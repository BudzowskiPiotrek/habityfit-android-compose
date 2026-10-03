package com.piodev.data.model

data class Exercise(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val equipmentType: String?,

    val muscleGroups: List<MuscleGroup> = emptyList()
)