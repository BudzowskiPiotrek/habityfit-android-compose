package com.piodev.habityfit.features.measurements.domain.model

data class Body(
    val id: String,
    val date: Long,
    val weight: Double,
    val muscleMass: Double?,
    val fatMass: Double?,
    val bodyWater: Double?,
)