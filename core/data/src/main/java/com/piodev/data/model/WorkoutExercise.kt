package com.piodev.data.model

data class WorkoutExercise(
    val id: String,
    val exerciseId: String,
    val exerciseName: String = "",
    val order: Int,
    val sets: List<ExerciseSet> = emptyList(),
    val restSecond: Int
)