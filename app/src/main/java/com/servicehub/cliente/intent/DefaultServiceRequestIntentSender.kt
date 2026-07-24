package com.servicehub.cliente.intent

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.servicehub.cliente.data.model.SolicitudServicio

/** Sends the request through an explicit intent to the next app. */
class DefaultServiceRequestIntentSender(
    private val context: Context
) : ServiceRequestIntentSender {
    override fun send(request: SolicitudServicio): Result<Unit> = runCatching {
        val intent = Intent(ServiceRequestIntentContract.ACTION_SUBMIT_REQUEST).apply {
            component = ComponentName(
                context.packageName,
                ServiceRequestIntentContract.TARGET_ACTIVITY
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(ServiceRequestIntentContract.EXTRA_VERSION, 1)
            putExtra(ServiceRequestIntentContract.EXTRA_PAYLOAD, ServiceRequestIntentContract.buildPayload(request))
        }

        try {
            context.startActivity(intent)
        } catch (error: ActivityNotFoundException) {
            throw IllegalStateException("No se encontró una activity receptora para la solicitud.", error)
        }
    }
}
