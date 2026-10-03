package com.piodev.network.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class SyncRequestDto(
    @Json(name = "USER_ID") val userId: String,
    @Json(name = "LAST_SYNC") val lastSync: Date
)