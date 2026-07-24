package com.servicehub.cliente.receiver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.servicehub.cliente.R
import com.servicehub.cliente.data.model.SolicitudServicio
import com.servicehub.cliente.intent.ServiceRequestIntentContract

class RequestReceiverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val request = ServiceRequestIntentContract.extractRequest(intent)
        if (request == null) {
            finish()
            return
        }

        setContent {
            MaterialTheme {
                RequestReceiverScreen(request = request, onClose = { finish() })
            }
        }
    }
}

@Composable
private fun RequestReceiverScreen(
    request: SolicitudServicio,
    onClose: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(text = stringResource(R.string.request_received_title), style = MaterialTheme.typography.headlineSmall)
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))
            Text(text = stringResource(R.string.request_received_client, request.clientName))
            Text(text = stringResource(R.string.request_received_service, request.serviceType))
            Text(text = stringResource(R.string.request_received_description, request.description))
            Text(text = stringResource(R.string.request_received_location, request.latitude, request.longitude))

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 24.dp))
            Button(onClick = onClose) {
                Text(text = stringResource(R.string.request_received_close))
            }
        }
    }
}
