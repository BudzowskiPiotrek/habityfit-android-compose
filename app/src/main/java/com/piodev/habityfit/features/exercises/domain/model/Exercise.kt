package com.piodev.habityfit.features.exercises.domain.model

data class Exercise(
    val id: String,
    val name: String,
    val equipment: String?,
    val description: String?,
    val muscleGroups: List<MuscleGroup>,
    val imageUrl: String?
)