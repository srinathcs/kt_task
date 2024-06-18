package com.tk.tktask.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.login.repository.LoginRepository
import com.tk.tktask.login.viewmodel.LoginViewModel
import com.tk.tktask.login.viewmodel.LoginViewModelFactory
import com.tk.tktask.login.widgets.EmailLoginFieldView
import com.tk.tktask.login.widgets.LoginButton
import com.tk.tktask.login.widgets.PasswordLoginFieldView
import com.tk.tktask.ui.theme.TKTaskTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TKTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val factory = LoginViewModelFactory(LoginRepository())
    val viewModel: LoginViewModel = viewModel(factory = factory)
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Login",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))

                EmailLoginFieldView()

                Spacer(modifier = Modifier.padding(top = 10.dp))

                PasswordLoginFieldView()

                Spacer(modifier = Modifier.padding(top = 10.dp))

                LoginButton()
            }
        }
    }
}
