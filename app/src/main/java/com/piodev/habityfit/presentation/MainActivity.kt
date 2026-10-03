package com.piodev.habityfit.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.piodev.habityfit.core.theme.SetpointTheme
import com.piodev.habityfit.features.auth.presentation.ui.LoginScreen
import com.piodev.habityfit.features.app.ExpiredScreen
import com.piodev.habityfit.features.app.AppScreen
import com.piodev.habityfit.features.app.TermsAcceptanceScreen
import com.piodev.habityfit.features.auth.presentation.ui.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inviteToken = intent?.data?.getQueryParameter("token")
        mainViewModel.setInviteToken(inviteToken)
        enableEdgeToEdge()
        setContent {
            SetpointTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val screen = mainViewModel.currentScreen) {
                        is MainViewModel.Screen.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is MainViewModel.Screen.Register -> {
                                RegisterScreen(
                                    onAlreadyHaveAccount = {
                                        mainViewModel.navigateTo(MainViewModel.Screen.Login)
                                    }
                                )
                        }

                        is MainViewModel.Screen.Login -> {
                            LoginScreen(
                                inviteToken = inviteToken,
                                onLoginSuccess = {
                                    mainViewModel.checkSession()
                                },
                                onRegisterClick = {
                                    mainViewModel.navigateTo(MainViewModel.Screen.Register)
                                }
                            )
                        }

                        is MainViewModel.Screen.Home -> {
                            AppScreen(
                                onLogout = {
                                    mainViewModel.logout()
                                }
                            )
                        }

                        is MainViewModel.Screen.Terms -> {
                            TermsAcceptanceScreen(
                                onAccept = {
                                    mainViewModel.checkSession()
                                }
                            )
                        }

                        is MainViewModel.Screen.Expired -> {
                            ExpiredScreen()
                        }
                    }
                }
            }
        }
    }
}