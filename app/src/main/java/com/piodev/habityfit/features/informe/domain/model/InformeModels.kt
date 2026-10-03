package com.piodev.habityfit.features.informe.domain.model

enum class InformePeriod {
    WEEK,
    MONTH,
    LAST_3_MONTHS,
    YEAR
}

data class MuscleGroupStat(
    val muscleGroupId: String,
    val muscleGroupName: String,
    val totalVolume: Double,
    val totalSets: Int,
    val minSets: Int?,
    val maxSets: Int?
)

data class InformeSummary(
    val totalWorkouts: Int,
    val totalDurationMinutes: Int,
    val totalVolume: Double,
    val totalSets: Int,
    val muscleGroupStats: List<MuscleGroupStat>
)