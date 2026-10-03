package com.piodev.habityfit.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.data.repository.SyncRepository
import com.piodev.network.dto.common.NetworkHelper
import com.piodev.network.dto.common.SessionManager
import com.piodev.habityfit.core.config.LimitsConfig
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val networkHelper: NetworkHelper,
    private val syncRepository: SyncRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    sealed class Screen {
        object Loading : Screen()
        object Login : Screen()
        object Register : Screen()
        object Home : Screen()
        object Expired : Screen()
        object Terms : Screen()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT, ESTADO, NAVEGACION
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var currentScreen by mutableStateOf<Screen>(Screen.Loading)
        private set
    private var inviteToken: String? = null

    init {
        checkSession()
    }

    fun setInviteToken(token: String?) {
        inviteToken = token
    }

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE LOGOUT, LIMPIA SESION Y VUELVE A CHEQUEAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun logout() {
        viewModelScope.launch {
            currentScreen = Screen.Loading
            authRepository.logout()
            checkSession()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ENTRADA CON SYNC MASIVO AL ENTRAR ( EN DEMO OPCION DESABILITADA )
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun checkSession() {
        val expirationDate = LimitsConfig.EXPIRATION_DATE
        val currentTime = System.currentTimeMillis()

        if (currentTime > expirationDate) {
            currentScreen = Screen.Expired
            return
        }

        viewModelScope.launch {
            val token = sessionManager.getToken()

            if (token.isNullOrEmpty()) {
                currentScreen = Screen.Login
            } else {
                val user = authRepository.getCurrentUser().first()
                val termsAcceptedAt = user?.termsAcceptedAt

                if (termsAcceptedAt == null || termsAcceptedAt < LimitsConfig.TERMS_REQUIRED_DATE) {
                    currentScreen = Screen.Terms
                    return@launch
                }

                currentScreen = Screen.Home
                if (networkHelper.hasInternet()) {
                    try {
                        syncRepository.syncAll()
                    } catch (e: Exception) {
//                        Log.e("SYNC", "Error en sync", e)
                    }
                }
            }
        }
    }
}