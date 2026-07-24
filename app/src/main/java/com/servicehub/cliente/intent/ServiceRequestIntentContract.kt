package com.servicehub.cliente.intent

import android.content.Intent
import android.os.Bundle
import com.servicehub.cliente.data.model.SolicitudServicio

/** Intent contract used to send the request to the next application. */
object ServiceRequestIntentContract {
    const val ACTION_SUBMIT_REQUEST = "com.servicehub.cliente.action.SUBMIT_REQUEST"
    const val EXTRA_PAYLOAD = "com.servicehub.cliente.extra.PAYLOAD"
    const val EXTRA_VERSION = "com.servicehub.cliente.extra.VERSION"

    const val TARGET_ACTIVITY = "com.servicehub.cliente.receiver.RequestReceiverActivity"

    fun buildPayload(request: SolicitudServicio): Bundle = Bundle().apply {
        putString("requestId", request.requestId)
        putString("clientName", request.clientName)
        putString("serviceType", request.serviceType)
        putString("description", request.description)
        putDouble("latitude", request.latitude)
        putDouble("longitude", request.longitude)
        putLong("timestamp", request.timestamp)
    }

    fun extractRequest(intent: Intent?): SolicitudServicio? {
        if (intent == null) return null
        val payload = intent.getBundleExtra(EXTRA_PAYLOAD) ?: return null

        return SolicitudServicio(
            requestId = payload.getString("requestId").orEmpty(),
            clientName = payload.getString("clientName").orEmpty(),
            serviceType = payload.getString("serviceType").orEmpty(),
            description = payload.getString("description").orEmpty(),
            latitude = payload.getDouble("latitude"),
            longitude = payload.getDouble("longitude"),
            timestamp = payload.getLong("timestamp")
        )
    }
}
