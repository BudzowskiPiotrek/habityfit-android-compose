package com.piodev.data.model

data class Routine(
    val id: String,
    val userId: String,
    val name: String,
    val description: String?,
    val exercises: List<RoutineExercise> = emptyList()
)