package com.servicehub.cliente.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.servicehub.cliente.location.MapCoordinates

/** 
 * Embeds Google Maps using the maps-compose library.
 * Allows the user to select a point on the map and shows a marker at the current selection.
 */
@Composable
fun ServiceHubMap(
    selectedCoordinates: MapCoordinates,
    onCoordinatesSelected: (MapCoordinates) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialLatLng = selectedCoordinates.toLatLng()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLatLng, 15f)
    }

    // Update camera when selectedCoordinates change (e.g., when obtaining current location)
    LaunchedEffect(selectedCoordinates) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(selectedCoordinates.toLatLng(), 15f)
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = false, // We handle location manually via ViewModel
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            myLocationButtonEnabled = false
        ),
        onMapClick = { latLng ->
            onCoordinatesSelected(MapCoordinates(latLng.latitude, latLng.longitude))
        }
    ) {
        Marker(
            state = MarkerState(position = selectedCoordinates.toLatLng()),
            title = "Ubicación seleccionada",
            draggable = false
        )
    }
}
