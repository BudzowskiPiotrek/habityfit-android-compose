package com.piodev.habityfit.features.routine.domain.model

data class Routine(
    val id: String,
    val name: String,
    val description: String?,
    val exercises: List<RoutineExercise> = emptyList()
)