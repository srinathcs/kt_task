package com.tk.tktask.location

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tk.tktask.location.repository.MapRepository
import com.tk.tktask.location.viewmodel.LocationViewModel
import com.tk.tktask.location.viewmodel.LocationViewModelFactory
import com.tk.tktask.realmdb.enitity.LocationEntity
import com.tk.tktask.ui.theme.TKTaskTheme

class ItemLocationTrackActivity : ComponentActivity() {
    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = LocationViewModelFactory(MapRepository())
        viewModel =
            ViewModelProvider(this, factory)[LocationViewModel::class.java]
        val timestamp = intent.getLongExtra("TIMESTAMP", -1)
        val latitude = intent.getDoubleExtra("LATITUDE", Double.NaN)
        val longitude = intent.getDoubleExtra("LONGITUDE", Double.NaN)
        val email = intent.getStringExtra("EMAIL") ?: ""

        val location = LocationEntity().apply {
            this.timestamp = if (timestamp != -1L) timestamp else null
            this.latitude = if (!latitude.isNaN()) latitude else null
            this.longitude = if (!longitude.isNaN()) longitude else null
            this.email = email
        }
        setContent {
            TKTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SingleLocation(location = location)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SingleLocation(location: LocationEntity) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    LaunchedEffect(permissionState) {
        permissionState.launchPermissionRequest()
    }

    if (permissionState.status.isGranted) {
        AndroidView(
            factory = {
                mapView.apply {
                    onCreate(Bundle())
                    getMapAsync { googleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = true

                        val position = LatLng(location.latitude ?: 0.0, location.longitude ?: 0.0)
                        googleMap.moveCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(position, 20f)
                            )
                        )
                        googleMap.clear()
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title(location.email)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { mapView ->
                mapView.onResume()
            }
        )
    } else {

    }

    DisposableEffect(mapView) {
        onDispose {
            mapView.onDestroy()
        }
    }
}