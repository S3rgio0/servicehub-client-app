package com.servicehub.cliente.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.servicehub.cliente.ui.screens.HomeScreen
import com.servicehub.cliente.ui.screens.RequestSummaryScreen
import com.servicehub.cliente.ui.screens.ServiceRequestScreen
import com.servicehub.cliente.viewmodel.ServiceRequestViewModel

private object Routes {
    const val HOME = "home"
    const val REQUEST = "request"
    const val SUMMARY = "summary"
}

@Composable
fun ServiceHubNavGraph(
    viewModel: ServiceRequestViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onStartRequest = { navController.navigate(Routes.REQUEST) })
        }
        composable(Routes.REQUEST) {
            ServiceRequestScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSummary = { navController.navigate(Routes.SUMMARY) }
            )
        }
        composable(Routes.SUMMARY) {
            RequestSummaryScreen(
                viewModel = viewModel,
                onNewRequest = {
                    viewModel.resetForNewRequest()
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
