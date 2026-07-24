package com.servicehub.cliente.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.servicehub.cliente.location.MapCoordinates

/** Embeds the native Google Maps SDK inside Compose and lets the user pick a point. */
@Composable
fun ServiceHubMap(
    selectedCoordinates: MapCoordinates,
    onCoordinatesSelected: (MapCoordinates) -> Unit,
    modifier: Modifier = Modifier
) {
    val mapView = rememberMapViewWithLifecycle()
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { view ->
            if (googleMap == null) {
                view.getMapAsync { map ->
                    googleMap = map
                    map.uiSettings.isZoomControlsEnabled = true
                    map.uiSettings.isCompassEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = false
                    map.setOnMapClickListener { latLng ->
                        onCoordinatesSelected(MapCoordinates(latLng.latitude, latLng.longitude))
                    }
                    renderSelectedPoint(map, selectedCoordinates)
                }
            }
        }
    )

    LaunchedEffect(selectedCoordinates, googleMap) {
        googleMap?.let { map ->
            renderSelectedPoint(map, selectedCoordinates)
        }
    }
}

private fun renderSelectedPoint(map: GoogleMap, coordinates: MapCoordinates) {
    val latLng = coordinates.toLatLng()
    map.clear()
    map.addMarker(
        MarkerOptions()
            .position(latLng)
            .title("Ubicación seleccionada")
    )
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
}
