package com.tk.tktask.location

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tk.tktask.dashboard.widget.LocationMap
import com.tk.tktask.location.repository.MapRepository
import com.tk.tktask.location.viewmodel.LocationViewModel
import com.tk.tktask.location.viewmodel.LocationViewModelFactory
import com.tk.tktask.ui.theme.TKTaskTheme

class LocationHistoryActivity : ComponentActivity() {
    private lateinit var viewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = LocationViewModelFactory(MapRepository())
        viewModel =
            ViewModelProvider(this, factory)[LocationViewModel::class.java]
        setContent {
            TKTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationMap()
                }
            }
        }
    }
}