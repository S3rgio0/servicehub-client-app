package com.servicehub.cliente.data

import android.app.Application
import com.servicehub.cliente.intent.DefaultServiceRequestIntentSender
import com.servicehub.cliente.intent.ServiceRequestIntentSender
import com.servicehub.cliente.location.DefaultLocationRepository
import com.servicehub.cliente.location.LocationRepository
import com.servicehub.cliente.repository.ServiceCatalogRepository
import com.servicehub.cliente.viewmodel.ServiceRequestViewModel
import com.servicehub.cliente.viewmodel.ServiceRequestViewModelFactory

/** Simple dependency container for the application. */
object AppGraph {
    private lateinit var application: Application

    private val serviceCatalogRepository: ServiceCatalogRepository by lazy {
        ServiceCatalogRepository()
    }

    private val locationRepository: LocationRepository by lazy {
        DefaultLocationRepository(application.applicationContext)
    }

    private val intentSender: ServiceRequestIntentSender by lazy {
        DefaultServiceRequestIntentSender(application.applicationContext)
    }

    val viewModelFactory: ServiceRequestViewModelFactory
        get() = ServiceRequestViewModelFactory(
            serviceCatalogRepository = serviceCatalogRepository,
            locationRepository = locationRepository,
            intentSender = intentSender
        )

    val previewViewModel: ServiceRequestViewModel by lazy {
        val previewLocationRepository = object : LocationRepository {
            override fun isLocationEnabled(): Boolean = true

            override suspend fun getCurrentLocation(): Result<com.servicehub.cliente.location.MapCoordinates> {
                return Result.success(com.servicehub.cliente.location.MapCoordinates.DEFAULT)
            }
        }

        val previewIntentSender = object : ServiceRequestIntentSender {
            override fun send(request: com.servicehub.cliente.data.model.SolicitudServicio): Result<Unit> = Result.success(Unit)
        }

        ServiceRequestViewModel(
            serviceCatalogRepository = serviceCatalogRepository,
            locationRepository = previewLocationRepository,
            intentSender = previewIntentSender
        )
    }

    fun initialize(app: Application) {
        application = app
    }
}
