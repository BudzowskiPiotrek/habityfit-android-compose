package com.piodev.data.model

data class WorkoutSession(
    val id: String,
    val date: Long,
    val durationMinutes: Int,
    val notes: String?,
    val exercises: List<WorkoutExercise> = emptyList()
)