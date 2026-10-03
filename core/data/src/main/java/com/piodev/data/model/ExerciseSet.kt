package com.piodev.data.model

data class ExerciseSet(
    val id: String,
    val setNumber: Int,
    val reps: Int,
    val weight: Double?,
    val rpe: Double?
)