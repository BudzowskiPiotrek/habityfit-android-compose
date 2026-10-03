package com.piodev.habityfit.features.community.domain.model

import com.piodev.database.model.Sex

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val sex: Sex?,
    val birthDate: Long?,
    val height: Double?,
)
