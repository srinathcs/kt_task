package com.tk.tktask.signup.ui.widgets

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tk.tktask.login.LoginActivity

@Composable
fun LoginScreenButton() {
    val context = LocalContext.current
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    ) {
        Text("Login")
    }
}