package com.servicehub.cliente.location

import com.google.android.gms.maps.model.LatLng

/** Simple coordinate holder used across the UI and domain layers. */
data class MapCoordinates(
    val latitude: Double,
    val longitude: Double
) {
    fun toLatLng(): LatLng = LatLng(latitude, longitude)

    companion object {
        val DEFAULT = MapCoordinates(19.432608, -99.133209)
    }
}
