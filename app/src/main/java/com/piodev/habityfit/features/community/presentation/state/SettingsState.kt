package com.piodev.habityfit.features.community.presentation.state

import com.piodev.database.model.Sex

data class SettingsState(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val sex: Sex? = null,
    val birthDate: Long? = null,
    val height: Double? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)