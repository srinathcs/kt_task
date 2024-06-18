package com.tk.tktask.dashboard.widget

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.dashboard.viewmodel.DashboardViewModel

@Composable
fun SwitchUserButton(toggleDialog: () -> Unit) {
    val viewModel: DashboardViewModel = viewModel()
    val nonUser by viewModel.users.observeAsState(listOf())
    Log.e("TAG", "SwitchUserButton: ${nonUser.size}")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { toggleDialog() },
            modifier = Modifier.padding(10.dp)
        ) {
            val isNoUser = nonUser.isEmpty()
            val label = if (isNoUser) "Add Account" else "Switch User"
            Text(label, textAlign = TextAlign.Center)
        }
    }
}