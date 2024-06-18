package com.tk.tktask.login.widgets

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.dashboard.DashboardActivity
import com.tk.tktask.login.viewmodel.LoginViewModel

@Composable
fun LoginButton() {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val email = viewModel.getEmail()
            val password = viewModel.getPassword()

            viewModel.setEmailError(email.isEmpty())
            viewModel.setPasswordError(password.isEmpty())

            if (!viewModel.getEmailError() && !viewModel.getPasswordError()) {
                val loginSuccess = viewModel.login()
                if (loginSuccess) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, DashboardActivity::class.java))
                } else {
                    Toast.makeText(
                        context,
                        "No account found. Please register.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    ) {
        Text("Login")
    }
}