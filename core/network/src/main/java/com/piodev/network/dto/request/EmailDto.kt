package com.piodev.network.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmailDto(
    @Json(name = "EMAIL") val email: String
)