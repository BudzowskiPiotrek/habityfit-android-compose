package com.piodev.network.dto.sync


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInvitationDto(
    @Json(name = "ID") val id: String,
    @Json(name = "EMAIL") val email: String,
    @Json(name = "SENDER_USER_ID") val senderUserId: String,
)