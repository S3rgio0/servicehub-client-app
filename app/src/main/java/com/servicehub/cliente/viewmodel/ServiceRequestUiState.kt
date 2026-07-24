package com.servicehub.cliente.viewmodel

import com.servicehub.cliente.data.model.ServiceType
import com.servicehub.cliente.data.model.SolicitudServicio
import com.servicehub.cliente.location.MapCoordinates

data class ServiceRequestUiState(
    val clientName: String = "",
    val clientNameError: String? = null,
    val serviceType: String = "",
    val serviceTypeError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val selectedCoordinates: MapCoordinates = MapCoordinates.DEFAULT,
    val hasLocationPermission: Boolean = false,
    val isGpsEnabled: Boolean = true,
    val isLoadingLocation: Boolean = false,
    val locationMessage: String? = null,
    val availableServiceTypes: List<ServiceType> = emptyList(),
    val lastSubmittedRequest: SolicitudServicio? = null
)
