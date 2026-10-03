package com.piodev.network.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    @Json(name = "statusCode") val statusCode: Int,
    @Json(name = "message") val message: String? = null,
    @Json(name = "result") val result: T? = null,
    @Json(name = "withError") val withError: Boolean = false
)