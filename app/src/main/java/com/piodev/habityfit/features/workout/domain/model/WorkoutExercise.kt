package com.piodev.habityfit.features.workout.domain.model

data class WorkoutExercise(
    val id: String,
    val exerciseId: String,
    val name: String,
    val order: Int,
    val rest_seconds: Int,
    val sets: List<WorkoutSet>,
    val imageUrl: String? = null
)