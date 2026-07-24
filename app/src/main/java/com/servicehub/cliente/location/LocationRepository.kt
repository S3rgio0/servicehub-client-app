package com.servicehub.cliente.location

interface LocationRepository {
    fun isLocationEnabled(): Boolean
    suspend fun getCurrentLocation(): Result<MapCoordinates>
}
