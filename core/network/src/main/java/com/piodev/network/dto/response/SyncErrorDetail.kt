package com.piodev.network.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncErrorDetailDto(
    @Json(name = "itemId")
    val itemIds: List<String>,

    @Json(name = "success")
    val success: List<Boolean>
)