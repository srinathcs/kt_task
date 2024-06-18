package com.tk.tktask.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tk.tktask.R
import com.tk.tktask.dashboard.DashboardActivity
import com.tk.tktask.signup.repository.SignUpRepository
import com.tk.tktask.signup.ui.SignUpActivity
import com.tk.tktask.signup.viewmodel.SignUpViewModel
import com.tk.tktask.signup.viewmodel.ViewModelFactory
import com.tk.tktask.ui.theme.TKTaskTheme
import kotlinx.coroutines.flow.collectLatest

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TKTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val factory = ViewModelFactory(SignUpRepository())
                    val viewModel: SignUpViewModel = viewModel(factory = factory)
                    val longedInStatus by viewModel.loggedInUser.observeAsState()
                    val loggedInUserEntity by viewModel.loggedInUserEntity.observeAsState()
                    LaunchedEffect(Unit) {
                        viewModel.findLoggedInUser()
                        viewModel.loggedInUserEntity.asFlow().collectLatest {
                            if (it != null) {
                                startActivity(
                                    Intent(
                                        this@SplashActivity,
                                        DashboardActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }
                    }

                    LaunchedEffect(longedInStatus, loggedInUserEntity) {
                        if (longedInStatus == true && loggedInUserEntity == null) {
                            startActivity(Intent(this@SplashActivity, SignUpActivity::class.java))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_kt),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}