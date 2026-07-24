package com.servicehub.cliente.data.model

/** Domain model sent to the next app through an explicit intent. */
data class SolicitudServicio(
    val requestId: String,
    val clientName: String,
    val serviceType: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
