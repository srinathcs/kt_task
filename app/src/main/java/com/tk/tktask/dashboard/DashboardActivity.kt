package com.tk.tktask.dashboard

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.tk.tktask.dashboard.repository.DashboardRepository
import com.tk.tktask.dashboard.viewmodel.DashboardViewModel
import com.tk.tktask.dashboard.viewmodel.DashboardViewModelFactory
import com.tk.tktask.dashboard.widget.LocationList
import com.tk.tktask.dashboard.widget.LocationTrackingButtons
import com.tk.tktask.dashboard.widget.UserCard
import com.tk.tktask.location.LocationHistoryActivity
import com.tk.tktask.login.repository.LoginRepository
import com.tk.tktask.manager.LocationWorker
import com.tk.tktask.signup.ui.SignUpActivity
import com.tk.tktask.ui.theme.Blue
import com.tk.tktask.ui.theme.TKTaskTheme
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


const val REQUEST_CHECK_SETTINGS = 123456
const val WORK_MANAGER_FILE_NAME = "LocationWorker"

class DashboardActivity : ComponentActivity() {
    private lateinit var viewModel: DashboardViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (!isGPSEnabled()) {
                    requestGPS()
                } else {
                    startLocationUpdates()
                }
            } else {
                Log.d("DashboardActivity", "Permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityResultCallback()
        val factory = DashboardViewModelFactory(DashboardRepository(), LoginRepository())
        viewModel =
            ViewModelProvider(this, factory)[DashboardViewModel::class.java]
        observeWorkManagerLocation()
        setContent {
            TKTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!isGPSEnabled()) {
                requestGPS()
            } else {
                onWorkManagerStatus()
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val constraints = Constraints.Builder().build()

            val locationWorkRequest = PeriodicWorkRequestBuilder<LocationWorker>(
                repeatInterval = 15,
                TimeUnit.MINUTES
            ).setConstraints(constraints).build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                WORK_MANAGER_FILE_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                locationWorkRequest
            )

            onWorkManagerStatus()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /*  private fun showDialogToEnableLocation() {
          AlertDialog.Builder(this)
              .setTitle("Enable Location")
              .setMessage("Location services need to be turned on to proceed.")
              .setPositiveButton("Enable") { _, _ ->
                  startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
              }
              .setNegativeButton("Cancel") { dialog, _ ->
                  dialog.dismiss()
              }
              .show()
      }*/

    private fun stopLocationUpdates() {
        WorkManager.getInstance(this).cancelUniqueWork(WORK_MANAGER_FILE_NAME)
        onWorkManagerStatus()
    }

    private fun observeWorkManagerLocation() {
        LocationWorker.data.observe(this) {
            if (this::viewModel.isInitialized && it != null) {
                val email = viewModel.currentUsers.value?.email.orEmpty()
                val item = it.apply {
                    this.email = email
                }
                viewModel.updateLocation(item)
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun requestGPS() {
        val locationRequest = LocationRequest.create().apply {
            interval = LocationWorker.LOCATION_UPDATE_INTERVAL
            fastestInterval = LocationWorker.LOCATION_UPDATE_INTERVAL / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(applicationContext)
                .checkLocationSettings(builder.build())
        result.addOnSuccessListener {
            Toast.makeText(applicationContext, "addOnSuccessListener", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "addOnFailureListener: " + it?.locationSettingsStates)
        }
        result.addOnFailureListener {
            Log.e("TAG", "addOnFailureListener: " + it.message.orEmpty())
            val resolvable = it as ResolvableApiException
            resolvable.startResolutionForResult(
                this@DashboardActivity,
                REQUEST_CHECK_SETTINGS
            )
        }
    }


    private fun onWorkManagerStatus() {
        if (getStateOfWork() != WorkInfo.State.ENQUEUED && getStateOfWork() != WorkInfo.State.RUNNING) {
            //startLocationUpdates()
            viewModel.setWorkManagerStatus(false)
            Log.e("startLocationUpdates", ": server started")
        } else {
            Log.e("startLocationUpdates", ": server already working")
            viewModel.setWorkManagerStatus(true)
        }
    }

    private fun getStateOfWork(): WorkInfo.State {
        return try {
            if (WorkManager.getInstance(this).getWorkInfosForUniqueWork(WORK_MANAGER_FILE_NAME)
                    .get()
                    .size > 0
            ) {
                WorkManager.getInstance(this).getWorkInfosForUniqueWork(WORK_MANAGER_FILE_NAME)
                    .get()[0].state
                // this can return WorkInfo.State.ENQUEUED or WorkInfo.State.RUNNING
                // you can check all of them in WorkInfo class.
            } else {
                WorkInfo.State.CANCELLED
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
            WorkInfo.State.CANCELLED
        } catch (e: InterruptedException) {
            e.printStackTrace()
            WorkInfo.State.CANCELLED
        }
    }

    private fun onActivityResultCallback() {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.e("TAG", "onActivityResult: " + it.resultCode)
            if (it.resultCode == REQUEST_CHECK_SETTINGS) {
                startLocationUpdates()
            }
        }
    }

    @Composable
    fun DashboardScreen() {
        var showDialog by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val toggleDialog = { showDialog = !showDialog }
        val currentUser by viewModel.currentUsers.observeAsState()
        val locations by viewModel.currentUserLocations.observeAsState(emptyList())
        val isWorkManagerIsActive by viewModel.isWorkManageIsActive.observeAsState()

        LaunchedEffect(Unit) {
            viewModel.fetchCurrentUser()
            viewModel.fetchNonCurrentUsers()
            viewModel.fetchCurrentUserLocations()
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentUser?.let { user ->
                    UserCard(user, {
                        if (viewModel.users.value.isNullOrEmpty()) {
                            stopLocationUpdates()
                            val intentSignUp =
                                Intent(this@DashboardActivity, SignUpActivity::class.java)
                            this@DashboardActivity.startActivity(intentSignUp)
                            finish()
                        } else {
                            showDialog = !showDialog
                        }
                    }, showDialog)
                } ?: run {
                    Text("No user logged in")
                }

                LocationTrackingButtons(
                    isWorkManagerIsActive,
                    startLocationUpdates = { startLocationUpdates() },
                    stopLocationUpdates = { stopLocationUpdates() }
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    ),
                    onClick = {
                        val mapIntent =
                            Intent(this@DashboardActivity, LocationHistoryActivity::class.java)
                        startActivity(mapIntent)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                ) {

                    Text("Tracked Location History", textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dialog_map),
                        contentDescription = "map"
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                LocationList(locations = locations, context)


                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}