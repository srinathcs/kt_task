package com.tk.tktask.signup.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.signup.viewmodel.SignUpViewModel

@Composable
fun UserNameFieldView() {
    val viewModel: SignUpViewModel = viewModel()
    val usernameError by viewModel.usernameError.observeAsState()
    val username by viewModel.username.observeAsState()
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = username.orEmpty(),
        onValueChange = {
            viewModel.setUsername(it)
            viewModel.setUsernameError(false)
        },
        label = { Text("Username") },
        isError = (usernameError == true)
    )
    if (usernameError == true) {
        Text(
            text = "Username is required",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}