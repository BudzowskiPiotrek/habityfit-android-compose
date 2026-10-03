package com.piodev.habityfit.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig.EMAIL_REGEX
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.auth.presentation.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEmailChange(email: String) {
        val error =
            if (email.isNotBlank() && !EMAIL_REGEX.matches(email)) "Formato de email incorrecto" else null
        _uiState.update {
            it.copy(
                email = email,
                error = null,
                emailError = error
            )
        }
    }

    fun register() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || !EMAIL_REGEX.matches(currentState.email)) {
            _uiState.update {
                it.copy(emailError = "Formato de email incorrecto")
            }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    isRegisterSuccess = false
                )
            }
            val result = authRepository.register(currentState.email)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRegisterSuccess = true
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
}