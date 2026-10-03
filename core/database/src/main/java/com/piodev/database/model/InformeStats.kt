package com.piodev.database.model

data class InformeSessionStatsRaw(
    val totalWorkouts: Int,
    val totalDuration: Int
)

data class InformeSetStatsRaw(
    val totalVolume: Double,
    val totalSets: Int
)

data class MuscleGroupStatRaw(
    val muscleGroupId: String,
    val muscleGroupName: String,
    val totalVolume: Double,
    val totalSets: Int,
    val minSets: Int?,
    val maxSets: Int?
)