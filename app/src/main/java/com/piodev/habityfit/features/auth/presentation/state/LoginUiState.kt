package com.piodev.habityfit.features.auth.presentation.state

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val fullName: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,

    val isInviteMode: Boolean = false,
    val inviteToken: String? = null,

    val passwordError: String? = null,
    val passwordRepeatError: String? = null,
    val fullNameError: String? = null,
    val emailError: String? = null
)