package com.piodev.habityfit.features.workout.domain.model

data class WorkoutSet(
    val id: String,
    val setNumber: Int,
    val reps: Int,
    val weight: Double?,
    val rpe: Double?
)