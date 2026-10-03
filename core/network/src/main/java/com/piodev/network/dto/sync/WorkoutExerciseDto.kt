package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class WorkoutExerciseDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "SESSION_ID") val sessionId: String,
    @Json(name = "EXERCISE_ID") val exerciseId: String,
    @Json(name = "SORT_ORDER") val order: Int,
    @Json(name = "REST_SECOND") val restSecond: Int,
)