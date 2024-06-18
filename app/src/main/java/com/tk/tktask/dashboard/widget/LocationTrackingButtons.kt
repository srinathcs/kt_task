package com.tk.tktask.dashboard.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tk.tktask.ui.theme.Blue
import com.tk.tktask.ui.theme.Green

@Composable
fun LocationTrackingButtons(
    isWorkManagerIsActive: Boolean?,
    startLocationUpdates: () -> Unit,
    stopLocationUpdates: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isWorkManagerIsActive == true) Green else Blue
            ),
            onClick = { startLocationUpdates() },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            if (isWorkManagerIsActive == true) {
                Text("Tracking Location", textAlign = TextAlign.Center)
            } else {
                Text("Track Location", textAlign = TextAlign.Center)
            }
        }

        if (isWorkManagerIsActive == true) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                onClick = { stopLocationUpdates() },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text("Stop\nTracking", textAlign = TextAlign.Center)
            }
        }
    }
}