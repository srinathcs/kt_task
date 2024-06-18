package com.tk.tktask.signup.ui.widgets

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.login.LoginActivity
import com.tk.tktask.signup.viewmodel.SignUpViewModel

@Composable
fun SignUpButton() {
    val viewModel: SignUpViewModel = viewModel()
    val context = LocalContext.current
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            viewModel.setEmailError(viewModel.getEmail().isEmpty())
            viewModel.setUsernameError(viewModel.getUsername().isEmpty())
            viewModel.setPasswordError(viewModel.getPassword().isEmpty())

            if (!viewModel.getEmailError() && !viewModel.getUsernameError() && !viewModel.getPasswordError()) {
                viewModel.addUser()
                context.startActivity(Intent(context, LoginActivity::class.java))
                Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Text("Sign Up")
    }
}