package com.servicehub.cliente.repository

import com.servicehub.cliente.data.model.ServiceType

/** Provides the supported service catalog. */
class ServiceCatalogRepository {
    fun getAvailableServices(): List<ServiceType> = ServiceType.entries
}
