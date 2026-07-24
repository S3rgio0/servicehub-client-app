package com.servicehub.cliente.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.servicehub.cliente.intent.ServiceRequestIntentSender
import com.servicehub.cliente.location.LocationRepository
import com.servicehub.cliente.repository.ServiceCatalogRepository

class ServiceRequestViewModelFactory(
    private val serviceCatalogRepository: ServiceCatalogRepository,
    private val locationRepository: LocationRepository,
    private val intentSender: ServiceRequestIntentSender
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceRequestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceRequestViewModel(serviceCatalogRepository, locationRepository, intentSender) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
