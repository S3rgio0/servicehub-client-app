package com.servicehub.cliente.intent

import com.servicehub.cliente.data.model.SolicitudServicio

interface ServiceRequestIntentSender {
    fun send(request: SolicitudServicio): Result<Unit>
}
