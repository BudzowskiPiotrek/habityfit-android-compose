package com.piodev.data.model

data class MuscleGroup(
    val id: String,
    val name: String,
    val description: String?,
    val minSets: Int? = null,
    val maxSets: Int? = null
)