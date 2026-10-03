package com.piodev.habityfit.features.graphics.domain.model

data class ExerciseProgressSeries(
    val exerciseId: String,
    val exerciseName: String,
    val dataPoints: List<ExerciseDataPoint>
)