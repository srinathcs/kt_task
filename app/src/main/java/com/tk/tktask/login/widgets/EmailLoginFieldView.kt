package com.tk.tktask.login.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.login.viewmodel.LoginViewModel

@Composable
fun EmailLoginFieldView() {
    val viewModel: LoginViewModel = viewModel()
    val emailError by viewModel.emailError.observeAsState()
    val email by viewModel.email.observeAsState()
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email.orEmpty(),
        onValueChange = {
            viewModel.setEmail(it)
            viewModel.setEmailError(false)
        },
        label = { Text("Email") },
        isError = (emailError == true)
    )
    if (emailError == true) {
        Text(
            text = "Email is required",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}