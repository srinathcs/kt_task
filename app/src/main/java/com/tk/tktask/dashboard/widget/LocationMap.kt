package com.tk.tktask.dashboard.widget

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.tk.tktask.location.viewmodel.LocationViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationMap() {
    val viewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    val locations by viewModel.location.observeAsState(emptyList())
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    DisposableEffect(mapView) {
        mapView.onCreate(null)
        mapView.onResume()
        onDispose {
            mapView.onDestroy()
        }
    }

    if (locationPermissionState.status.isGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            ) { view ->
                mapView.getMapAsync { map ->
                    googleMap = map
                    googleMap?.apply {
                        uiSettings.isZoomControlsEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                        if (locations.isNotEmpty()) {
                            val boundsBuilder = LatLngBounds.Builder()
                            val polylineOptions = PolylineOptions()

                            locations.forEach { location ->
                                val latLng =
                                    LatLng(location.latitude ?: 0.0, location.longitude ?: 0.0)
                                addMarker(MarkerOptions().position(latLng).title(location.email))
                                boundsBuilder.include(latLng)
                                polylineOptions.add(latLng)
                            }

                            addPolyline(polylineOptions)
                            val bounds = boundsBuilder.build()
                            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    googleMap?.let { map ->
                        val currentLocation = locations.lastOrNull()
                        currentLocation?.let { location ->
                            val latLng =
                                LatLng(location.latitude ?: 0.0, location.longitude ?: 0.0)
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .size(56.dp)
                    .padding(bottom = 15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Playback",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }
    } else {
        LaunchedEffect(Unit) {
            locationPermissionState.launchPermissionRequest()
        }
    }
}