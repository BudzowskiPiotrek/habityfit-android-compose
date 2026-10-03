package com.piodev.network.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AcceptInvitationDto(
    @Json(name = "TOKEN") val token: String,
    @Json(name = "FULL_NAME") val fullName: String,
    @Json(name = "PASSWORD") val password: String
)