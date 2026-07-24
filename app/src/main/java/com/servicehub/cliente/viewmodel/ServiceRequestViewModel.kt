package com.servicehub.cliente.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.servicehub.cliente.data.model.SolicitudServicio
import com.servicehub.cliente.intent.ServiceRequestIntentSender
import com.servicehub.cliente.location.LocationRepository
import com.servicehub.cliente.location.MapCoordinates
import com.servicehub.cliente.repository.ServiceCatalogRepository
import com.servicehub.cliente.utils.RequestValidator
import java.util.UUID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Owns the request form, map selection, validation and intent sending logic. */
class ServiceRequestViewModel(
    private val serviceCatalogRepository: ServiceCatalogRepository,
    private val locationRepository: LocationRepository,
    private val intentSender: ServiceRequestIntentSender
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ServiceRequestUiState(
            selectedCoordinates = MapCoordinates.DEFAULT,
            availableServiceTypes = serviceCatalogRepository.getAvailableServices()
        )
    )
    val uiState: StateFlow<ServiceRequestUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ServiceRequestUiEvent>()
    val events: SharedFlow<ServiceRequestUiEvent> = _events.asSharedFlow()

    fun onClientNameChanged(value: String) {
        _uiState.update { it.copy(clientName = value, clientNameError = null) }
    }

    fun onServiceTypeChanged(value: String) {
        _uiState.update { it.copy(serviceType = value, serviceTypeError = null) }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { it.copy(description = value, descriptionError = null) }
    }

    fun onMapCoordinatesChanged(coordinates: MapCoordinates) {
        _uiState.update { it.copy(selectedCoordinates = coordinates) }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        _uiState.update {
            it.copy(
                hasLocationPermission = granted,
                locationMessage = if (granted) null else "No concediste permiso de ubicación. Puedes elegir el punto manualmente en el mapa."
            )
        }

        if (granted) {
            refreshCurrentLocation()
        }
    }

    fun refreshCurrentLocation() {
        viewModelScope.launch {
            if (!_uiState.value.hasLocationPermission) {
                emitSnackbar("Necesitamos permiso de ubicación para usar tu posición actual.")
                return@launch
            }

            if (!locationRepository.isLocationEnabled()) {
                _uiState.update { it.copy(isGpsEnabled = false, locationMessage = "Activa el GPS para usar tu ubicación actual.") }
                emitSnackbar("El GPS está apagado. Puedes mover el marcador manualmente.")
                return@launch
            }

            _uiState.update { it.copy(isLoadingLocation = true, isGpsEnabled = true) }
            val result = locationRepository.getCurrentLocation()
            _uiState.update { it.copy(isLoadingLocation = false) }

            result
                .onSuccess { coordinates ->
                    _uiState.update {
                        it.copy(
                            selectedCoordinates = coordinates,
                            locationMessage = "Ubicación actual obtenida correctamente."
                        )
                    }
                    emitSnackbar("Ubicación actual lista.")
                }
                .onFailure {
                    _uiState.update { state -> state.copy(locationMessage = "No se pudo obtener la ubicación actual. Selecciona manualmente en el mapa.") }
                    emitSnackbar("No se pudo obtener la ubicación. Puedes continuar seleccionando el punto manualmente.")
                }
        }
    }

    fun submitRequest() {
        val currentState = _uiState.value
        val validation = RequestValidator.validate(
            clientName = currentState.clientName,
            serviceType = currentState.serviceType,
            description = currentState.description
        )

        if (validation.hasErrors) {
            _uiState.update {
                it.copy(
                    clientNameError = validation.clientNameError,
                    serviceTypeError = validation.serviceTypeError,
                    descriptionError = validation.descriptionError
                )
            }
            viewModelScope.launch { emitSnackbar("Revisa los campos obligatorios antes de buscar profesionales.") }
            return
        }

        val request = SolicitudServicio(
            requestId = UUID.randomUUID().toString(),
            clientName = currentState.clientName.trim(),
            serviceType = currentState.serviceType,
            description = currentState.description.trim(),
            latitude = currentState.selectedCoordinates.latitude,
            longitude = currentState.selectedCoordinates.longitude,
            timestamp = System.currentTimeMillis()
        )

        val sendResult = intentSender.send(request)
        sendResult
            .onSuccess {
                _uiState.update { it.copy(lastSubmittedRequest = request) }
                viewModelScope.launch { _events.emit(ServiceRequestUiEvent.NavigateToSummary) }
            }
            .onFailure { throwable ->
                viewModelScope.launch {
                    emitSnackbar(throwable.message ?: "No se pudo enviar la solicitud a la segunda app.")
                }
            }
    }

    fun resetForNewRequest() {
        _uiState.update {
            it.copy(
                clientName = "",
                clientNameError = null,
                serviceType = "",
                serviceTypeError = null,
                description = "",
                descriptionError = null,
                lastSubmittedRequest = null,
                locationMessage = null
            )
        }
    }

    private suspend fun emitSnackbar(message: String) {
        _events.emit(ServiceRequestUiEvent.ShowSnackbar(message))
    }
}
