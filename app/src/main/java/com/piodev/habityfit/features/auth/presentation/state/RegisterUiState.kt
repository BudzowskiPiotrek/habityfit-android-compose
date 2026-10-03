package com.piodev.habityfit.features.auth.presentation.state

data class RegisterUiState(
    val email: String = "",
    val emailError: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccess: Boolean = false
)