package com.piodev.data.model

import com.piodev.database.model.Sex

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val sex: Sex?,
    val birthDate: Long?,
    val height: Double?,
    val termsAcceptedAt: Long?
)