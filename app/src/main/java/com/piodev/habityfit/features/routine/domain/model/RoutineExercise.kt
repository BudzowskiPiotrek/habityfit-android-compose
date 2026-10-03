package com.piodev.habityfit.features.routine.domain.model

data class RoutineExercise(
    val id: String,
    val exerciseId: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val order: Int,
    val restSecond: Int,
    val targetWeight: Double?,
    val imageUrl: String? = null
)