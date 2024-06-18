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
fun PasswordFieldView() {
    val viewModel: SignUpViewModel = viewModel()
    val passwordError by viewModel.passwordError.observeAsState()
    val password by viewModel.password.observeAsState()
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password.orEmpty(),
        onValueChange = {
            viewModel.setPassword(it)
            viewModel.setPasswordError(false)
        },
        label = { Text("Password") },
        isError = (passwordError == true)
    )
    if (passwordError == true) {
        Text(
            text = "Password is required",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}