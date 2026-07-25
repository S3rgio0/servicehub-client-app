package com.servicehub.cliente.intent

import android.content.Intent
import com.servicehub.cliente.data.model.SolicitudServicio

/** Intent contract used to send the request to the second application. */
object ServiceRequestIntentContract {
    const val ACTION_SUBMIT_REQUEST = "com.servicehub.cliente.action.SUBMIT_REQUEST"
    
    // Explicit constants as requested
    const val EXTRA_REQUEST_ID = "requestId"
    const val EXTRA_CLIENT_NAME = "clientName"
    const val EXTRA_SERVICE_TYPE = "serviceType"
    const val EXTRA_DESCRIPTION = "description"
    const val EXTRA_LATITUDE = "latitude"
    const val EXTRA_LONGITUDE = "longitude"
    const val EXTRA_TIMESTAMP = "timestamp"

    // Target application details
    const val TARGET_PACKAGE = "com.servicehub.profesional"
    const val TARGET_ACTIVITY = "com.servicehub.profesional.RequestReceiverActivity"

    fun extractRequest(intent: Intent?): SolicitudServicio? {
        if (intent == null) return null
        
        val requestId = intent.getStringExtra(EXTRA_REQUEST_ID) ?: return null
        val clientName = intent.getStringExtra(EXTRA_CLIENT_NAME) ?: return null
        val serviceType = intent.getStringExtra(EXTRA_SERVICE_TYPE) ?: return null
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: return null
        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, 0L)

        return SolicitudServicio(
            requestId = requestId,
            clientName = clientName,
            serviceType = serviceType,
            description = description,
            latitude = latitude,
            longitude = longitude,
            timestamp = timestamp
        )
    }
}
