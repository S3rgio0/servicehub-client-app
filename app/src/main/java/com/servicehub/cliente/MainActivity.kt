package com.servicehub.cliente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.servicehub.cliente.data.AppGraph
import com.servicehub.cliente.ui.navigation.ServiceHubNavGraph
import com.servicehub.cliente.ui.theme.ServiceHubClienteTheme
import com.servicehub.cliente.viewmodel.ServiceRequestViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ServiceRequestViewModel by viewModels { AppGraph.viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceHubClienteTheme {
                ServiceHubNavGraph(viewModel = viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewApp() {
    ServiceHubClienteTheme {
        ServiceHubNavGraph(viewModel = AppGraph.previewViewModel)
    }
}
