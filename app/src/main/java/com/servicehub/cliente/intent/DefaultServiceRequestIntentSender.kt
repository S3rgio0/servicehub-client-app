package com.servicehub.cliente.intent

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.servicehub.cliente.data.model.SolicitudServicio

/** Sends the request through an explicit intent to the second app (ServiceHub Profesional). */
class DefaultServiceRequestIntentSender(
    private val context: Context
) : ServiceRequestIntentSender {
    override fun send(request: SolicitudServicio): Result<Unit> = runCatching {
        val intent = Intent(ServiceRequestIntentContract.ACTION_SUBMIT_REQUEST).apply {
            component = ComponentName(
                ServiceRequestIntentContract.TARGET_PACKAGE,
                ServiceRequestIntentContract.TARGET_ACTIVITY
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            // Put extras using individual constants as requested
            putExtra(ServiceRequestIntentContract.EXTRA_REQUEST_ID, request.requestId)
            putExtra(ServiceRequestIntentContract.EXTRA_CLIENT_NAME, request.clientName)
            putExtra(ServiceRequestIntentContract.EXTRA_SERVICE_TYPE, request.serviceType)
            putExtra(ServiceRequestIntentContract.EXTRA_DESCRIPTION, request.description)
            putExtra(ServiceRequestIntentContract.EXTRA_LATITUDE, request.latitude)
            putExtra(ServiceRequestIntentContract.EXTRA_LONGITUDE, request.longitude)
            putExtra(ServiceRequestIntentContract.EXTRA_TIMESTAMP, request.timestamp)
        }

        try {
            context.startActivity(intent)
        } catch (error: ActivityNotFoundException) {
            // If the explicit intent fails because the app is not installed, we can try an implicit one
            // or just inform the user. The requirement asks for an explicit intent.
            throw IllegalStateException("La aplicación 'ServiceHub Profesional' no está instalada o no puede recibir la solicitud.", error)
        }
    }
}
