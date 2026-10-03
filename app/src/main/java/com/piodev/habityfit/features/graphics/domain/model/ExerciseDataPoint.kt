package com.piodev.habityfit.features.graphics.domain.model

data class ExerciseDataPoint(
    val sessionDate: Long,
    val maxWeight: Double,
    val totalVolume: Double,
    val estimatedOneRM: Double,
    val bestReps: Int,
    val totalSets: Int
)