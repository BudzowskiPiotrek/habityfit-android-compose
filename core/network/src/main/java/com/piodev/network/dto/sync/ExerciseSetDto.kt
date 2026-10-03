package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ExerciseSetDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "WORKOUT_EXERCISE_ID") val workoutExerciseId: String,
    @Json(name = "SET_NUMBER") val setNumber: Int,
    @Json(name = "REPS") val reps: Int,
    @Json(name = "WEIGHT") val weight: Double?,
    @Json(name = "RPE") val rpe: Double?,
)