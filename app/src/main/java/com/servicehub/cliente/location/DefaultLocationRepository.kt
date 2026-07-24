package com.servicehub.cliente.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

/** Default implementation backed by Fused Location Provider. */
class DefaultLocationRepository(
    private val context: Context
) : LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    override fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<MapCoordinates> = runCatching {
        val tokenSource = CancellationTokenSource()
        val currentLocation = fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .await() ?: fusedLocationClient.lastLocation.await()

        currentLocation?.let {
            MapCoordinates(it.latitude, it.longitude)
        } ?: throw IllegalStateException("No se pudo obtener la ubicación actual.")
    }
}
