package com.piodev.habityfit.features.auth.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val termsAcceptedAt: Long? = null
)