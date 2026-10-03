package com.piodev.data.model

data class BodyMeasurement(
    val id: String,
    val idUser: String,
    val date: Long,
    val weight: Double,
    val muscleMass: Double?,
    val fatMass: Double?,
    val bodyWater: Double?
)