package com.tk.tktask.dashboard.widget

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tk.tktask.location.ItemLocationTrackActivity
import com.tk.tktask.realmdb.enitity.LocationEntity
import com.tk.tktask.ui.theme.Blue
import com.tk.tktask.ui.theme.Green
import java.util.Date
import java.util.Locale

@Composable
fun LocationList(locations: List<LocationEntity>, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (locations.isNotEmpty()) {
            LazyColumn {
                itemsIndexed(locations) { index, location ->
                    LocationItem(
                        location,
                        onItemClick = { navigateToMapActivity(location, context) })
                }
            }
        } else {
            Text("No locations found.")
        }
    }
}

@Composable
fun LocationItem(location: LocationEntity, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = formatTimestamp(location.timestamp).uppercase(),
                fontWeight = FontWeight.Medium,
                color = Blue
            )
            Text(
                text = "Latitude: ${location.latitude ?: "Unknown"}",
                fontWeight = FontWeight.Bold,
                color = Green
            )
            Text(
                text = "Longitude: ${location.longitude ?: "Unknown"}",
                fontWeight = FontWeight.Bold,
                color = Green
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun formatTimestamp(timestamp: Long?): String {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.getDefault())
        val date = Date(timestamp)
        sdf.format(date)
    } else {
        "Unknown"
    }
}

fun navigateToMapActivity(location: LocationEntity, context: Context) {
    val intent = Intent(context, ItemLocationTrackActivity::class.java).apply {
        putExtra("TIMESTAMP", location.timestamp)
        putExtra("LATITUDE", location.latitude)
        putExtra("LONGITUDE", location.longitude)
        putExtra("EMAIL", location.email)
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
