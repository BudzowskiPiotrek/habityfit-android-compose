package com.piodev.network.dto.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json
import com.piodev.network.dto.sync.UserDto
import java.util.Date

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    @Json(name = "Token") val token: String,
    @Json(name = "User") val user: UserDto,
    @Json(name = "Expiration") val expiration: Date? = null
)