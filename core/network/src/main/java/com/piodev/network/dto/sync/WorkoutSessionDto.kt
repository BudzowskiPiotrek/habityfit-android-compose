package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class WorkoutSessionDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "USER_ID") val userId: String,
    @Json(name = "DATE") val date: Date,
    @Json(name = "DURATION") val durationMinutes: Int,
    @Json(name = "NOTES") val notes: String?,
)