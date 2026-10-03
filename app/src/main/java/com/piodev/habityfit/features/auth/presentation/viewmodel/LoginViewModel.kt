package com.piodev.habityfit.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig.EMAIL_REGEX
import com.piodev.habityfit.core.config.LimitsConfig.MAX_PASSWORD_LENGTH
import com.piodev.habityfit.core.config.LimitsConfig.MIN_PASSWORD_LENGTH
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.auth.presentation.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT, ESTADO Y EVENTOS DE FORMULARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun initWithToken(token: String) {
        _uiState.update { it.copy(inviteToken = token, isInviteMode = true) }
    }

    fun onPasswordChange(password: String) {
        val error = validatePassword(password)
        _uiState.update { it.copy(password = password, error = null, passwordError = error) }
    }

    fun onPasswordRepeatChange(v: String) {
        val error = if (v != _uiState.value.password) "Las contraseñas no coinciden" else null
        _uiState.update { it.copy(passwordRepeat = v, error = null, passwordRepeatError = error) }
    }

    fun onFullNameChange(v: String) {
        val error = if (v.isBlank()) "El nombre no puede estar vacío" else null
        _uiState.update { it.copy(fullName = v, error = null, fullNameError = error) }
    }

    fun onEmailChange(email: String) {
        val error =
            if (email.isNotBlank() && !EMAIL_REGEX.matches(email)) "Formato de email incorrecto" else null
        _uiState.update { it.copy(email = email, error = null, emailError = error) }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE LOGIN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update {
                it.copy(error = "Email y contraseña requeridos")
            }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null, isLoginSuccess = false)
            }
            val result = authRepository.login(
                currentState.email,
                currentState.password
            )
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error desconocido"
                        )
                    }
                }
            )
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE INVITACION
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun acceptInvite() {
        val s = _uiState.value
        val nameError = if (s.fullName.isBlank()) "El nombre no puede estar vacío" else null
        val passError = validatePassword(s.password)
        val repeatError =
            if (s.password != s.passwordRepeat) "Las contraseñas no coinciden" else null

        if (nameError != null || passError != null || repeatError != null) {
            _uiState.update {
                it.copy(
                    fullNameError = nameError,
                    passwordError = passError,
                    passwordRepeatError = repeatError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.accept(s.inviteToken ?: "", s.fullName, s.password)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error desconocido"
                        )
                    }
                }
            )
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.length < MIN_PASSWORD_LENGTH -> "Mínimo $MIN_PASSWORD_LENGTH caracteres"
            password.length > MAX_PASSWORD_LENGTH -> "Máximo $MAX_PASSWORD_LENGTH caracteres"

            !password.any { it.isUpperCase() } -> "Debe contener al menos una mayúscula"

            !password.any { it.isDigit() } -> "Debe contener al menos un número"

            password.contains("'") || password.contains("\"") -> "No se permiten comillas"
            else -> null
        }
    }
}