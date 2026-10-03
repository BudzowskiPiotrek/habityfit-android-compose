package com.piodev.habityfit.features.workout.domain.model

data class WorkoutSession(
    val id: String,
    val date: Long,
    val durationMinutes: Int,
    val notes: String?,
    val exercises: List<WorkoutExercise> = emptyList()
)