package com.servicehub.cliente.viewmodel

sealed class ServiceRequestUiEvent {
    data class ShowSnackbar(val message: String) : ServiceRequestUiEvent()
    data object NavigateToSummary : ServiceRequestUiEvent()
}
