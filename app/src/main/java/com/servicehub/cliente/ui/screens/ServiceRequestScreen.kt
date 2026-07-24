package com.servicehub.cliente.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.servicehub.cliente.maps.ServiceHubMap
import com.servicehub.cliente.ui.components.ServiceHubTopAppBar
import com.servicehub.cliente.ui.components.ServiceInfoCard
import com.servicehub.cliente.ui.components.ServiceTypeDropdown
import com.servicehub.cliente.viewmodel.ServiceRequestUiEvent
import com.servicehub.cliente.viewmodel.ServiceRequestViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ServiceRequestScreen(
    viewModel: ServiceRequestViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSummary: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        viewModel.onLocationPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ServiceRequestUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                ServiceRequestUiEvent.NavigateToSummary -> onNavigateToSummary()
            }
        }
    }

    Scaffold(
        topBar = {
            ServiceHubTopAppBar(
                title = "Solicitar Servicio",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!uiState.hasLocationPermission) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        viewModel.refreshCurrentLocation()
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Usar ubicación actual")
            }
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val mapCardHeight = maxOf(280.dp, maxHeight * 0.52f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mapCardHeight),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ServiceHubMap(
                            selectedCoordinates = uiState.selectedCoordinates,
                            onCoordinatesSelected = viewModel::onMapCoordinatesChanged,
                            modifier = Modifier.fillMaxSize()
                        )

                        if (uiState.isLoadingLocation) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }

                ServiceInfoCard(
                    title = "Ubicación seleccionada",
                    message = "Latitud: ${"%.6f".format(uiState.selectedCoordinates.latitude)} | Longitud: ${"%.6f".format(uiState.selectedCoordinates.longitude)}"
                )

                uiState.locationMessage?.let { message ->
                    ServiceInfoCard(
                        title = "Estado de ubicación",
                        message = message
                    )
                }

                Text(text = "Datos de la solicitud", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = uiState.clientName,
                    onValueChange = viewModel::onClientNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre del cliente") },
                    isError = uiState.clientNameError != null,
                    supportingText = uiState.clientNameError?.let { { Text(it) } }
                )

                ServiceTypeDropdown(
                    value = uiState.serviceType,
                    options = uiState.availableServiceTypes.map { it.displayName },
                    onValueSelected = viewModel::onServiceTypeChanged,
                    label = "Tipo de servicio",
                    isError = uiState.serviceTypeError != null,
                    supportingText = uiState.serviceTypeError,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    label = { Text("Descripción del problema o necesidad") },
                    isError = uiState.descriptionError != null,
                    supportingText = uiState.descriptionError?.let { { Text(it) } }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = viewModel::submitRequest,
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(text = "Buscar Profesionales")
                }
            }
        }
    }
}
