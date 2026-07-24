package com.servicehub.cliente.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.servicehub.cliente.ui.components.RequestDetailRow
import com.servicehub.cliente.ui.components.ServiceHubTopAppBar
import com.servicehub.cliente.utils.DateFormatUtils
import com.servicehub.cliente.viewmodel.ServiceRequestViewModel

@Composable
fun RequestSummaryScreen(
    viewModel: ServiceRequestViewModel,
    onNewRequest: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val request = uiState.lastSubmittedRequest

    Scaffold(
        topBar = {
            ServiceHubTopAppBar(title = "Resumen de solicitud")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Solicitud enviada", style = MaterialTheme.typography.headlineSmall)
                    if (request == null) {
                        Text(text = "No hay una solicitud reciente para mostrar.")
                    } else {
                        RequestDetailRow(label = "ID", value = request.requestId)
                        RequestDetailRow(label = "Cliente", value = request.clientName)
                        RequestDetailRow(label = "Servicio", value = request.serviceType)
                        RequestDetailRow(label = "Descripción", value = request.description)
                        RequestDetailRow(
                            label = "Coordenadas",
                            value = "${"%.6f".format(request.latitude)}, ${"%.6f".format(request.longitude)}"
                        )
                        RequestDetailRow(label = "Fecha y hora", value = DateFormatUtils.format(request.timestamp))
                    }
                }
            }

            Button(
                onClick = onNewRequest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Nueva Solicitud")
            }
        }
    }
}
