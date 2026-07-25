package com.servicehub.cliente.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.servicehub.cliente.location.MapCoordinates
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

/**
 * Embeds OpenStreetMap (OSM) using the osmdroid library.
 * This removes the requirement for a Google Maps API Key.
 */
@Composable
fun ServiceHubMap(
    selectedCoordinates: MapCoordinates,
    onCoordinatesSelected: (MapCoordinates) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Configure osmdroid (required before creating MapView)
    remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        Configuration.getInstance().userAgentValue = context.packageName
        true
    }

    val mapView = remember { MapView(context) }
    val marker = remember { Marker(mapView) }

    AndroidView(
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                
                // Set initial zoom and position
                controller.setZoom(17.0)
                controller.setCenter(GeoPoint(selectedCoordinates.latitude, selectedCoordinates.longitude))

                // Listener for clicks on the map
                val overlayEvents = MapEventsOverlay(object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                        onCoordinatesSelected(MapCoordinates(p.latitude, p.longitude))
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint): Boolean = false
                })
                overlays.add(overlayEvents)
                
                // Configure marker
                marker.title = "Ubicación seleccionada"
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                overlays.add(marker)
            }
        },
        modifier = modifier,
        update = { view ->
            val geoPoint = GeoPoint(selectedCoordinates.latitude, selectedCoordinates.longitude)
            marker.position = geoPoint
            view.controller.animateTo(geoPoint)
            view.invalidate() // Refresh map
        }
    )

    // Cleanup when the composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            mapView.onDetach()
        }
    }
}
