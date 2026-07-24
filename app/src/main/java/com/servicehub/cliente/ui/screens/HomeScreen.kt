package com.servicehub.cliente.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.servicehub.cliente.ui.theme.ServiceHubBlue
import com.servicehub.cliente.ui.theme.ServiceHubBlueDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartRequest: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("ServiceHub Cliente") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(ServiceHubBlue.copy(alpha = 0.16f), MaterialTheme.colorScheme.background)
                    )
                )
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(ServiceHubBlue, ServiceHubBlueDark)))
                            .padding(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }

                    Text(
                        text = "ServiceHub Cliente",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Solicita servicios profesionales cercanos de forma rápida, precisa y con ubicación en mapa.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(onClick = onStartRequest) {
                        Text(text = "Solicitar Servicio")
                    }
                }
            }
        }
    }
}
