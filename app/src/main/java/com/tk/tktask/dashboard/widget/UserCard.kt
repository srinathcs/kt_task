package com.tk.tktask.dashboard.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.dashboard.viewmodel.DashboardViewModel
import com.tk.tktask.realmdb.enitity.UserEntity

@Composable
fun UserCard(user: UserEntity, toggleDialog: () -> Unit, showDialog: Boolean) {
    val viewModel: DashboardViewModel = viewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrentUserCard(user)

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = user.username.uppercase(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = user.email.uppercase(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            SwitchUserButton(toggleDialog)

            if (showDialog) {
                SwitchUserDialog(
                    onUserSelected = { selectedUser ->
                        viewModel.setCurrentUser(selectedUser)
                        viewModel.fetchCurrentUserLocations()
                    },
                    onDismiss = toggleDialog,
                    updateUser = { user -> viewModel.updateUser(user) }
                )
            }
        }
    }
}
