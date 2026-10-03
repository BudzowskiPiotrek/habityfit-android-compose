package com.piodev.network.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    @Json(name = "Email") val email: String,
    @Json(name = "Password") val password: String
)