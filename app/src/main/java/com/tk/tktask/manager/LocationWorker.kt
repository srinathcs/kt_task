package com.tk.tktask.manager

import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tk.tktask.realmdb.enitity.LocationEntity
import java.util.concurrent.CountDownLatch


class LocationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override suspend fun doWork(): Result {
        Log.d("LocationWorker", "doWork: Start location updates")

        val latch = CountDownLatch(1)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)

            val locationRequest = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL
                fastestInterval = LOCATION_UPDATE_INTERVAL / 2
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.let { result ->
                        result.locations.firstOrNull { (it != null) }?.let { location ->
                            Log.d(
                                "LocationWorker",
                                "New Location: ${location.latitude}, ${location.longitude}"
                            )
                            val locationEntity = LocationEntity().apply {
                                timestamp = System.currentTimeMillis()
                                latitude = location.latitude
                                longitude = location.longitude
                            }
                            mutableData.postValue(locationEntity)
                            latch.countDown()
                        }

                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            latch.await()
            fusedLocationClient.removeLocationUpdates(locationCallback)
            return Result.success()
        } else {
            Log.e("LocationWorker", "Permission not granted")
            return Result.failure()
        }
    }

    companion object {
        const val LOCATION_UPDATE_INTERVAL: Long = 1000 * 30
        val mutableData = MutableLiveData<LocationEntity>()
        val data: LiveData<LocationEntity> = mutableData
    }
}