package com.piodev.habityfit.features.graphics.data.mapper

import com.piodev.database.model.WorkoutSessionWithExercises
import com.piodev.habityfit.core.util.estimateOneRM
import com.piodev.habityfit.features.graphics.domain.model.ExerciseDataPoint
import com.piodev.habityfit.features.graphics.domain.model.ExerciseProgressSeries

fun List<WorkoutSessionWithExercises>.toProgressSeries(
    exerciseId: String,
    exerciseName: String,
    routineCreatedAt: Long? = null
): ExerciseProgressSeries {
    val points = this
        .mapNotNull { session ->
            val match = session.exerciseConfigs
                .firstOrNull { it.config.exerciseId == exerciseId }
                ?: return@mapNotNull null

            val sets = match.sets.filter { it.weight != null && it.weight!! > 0 }
            if (sets.isEmpty()) return@mapNotNull null
//            val order = match.config.order
//            val restSecond = match.config.restSecond


            val bestSet = sets.maxByOrNull { it.weight!! }!!
            val avgWeight = sets.map { it.weight!! }.average()
            val avgReps = sets.map { it.reps }.average().toInt().coerceAtLeast(1)

//            val effectiveWeight = calculateEffectiveWeight(
//                avgWeight,
//                order,
//                restSecond
//            )

            ExerciseDataPoint(
                sessionDate = session.session.date,
                maxWeight = bestSet.weight!!,
                totalVolume = sets.sumOf { (it.reps * (it.weight ?: 0.0)) },
                estimatedOneRM = estimateOneRM(avgWeight, avgReps),
                bestReps = bestSet.reps,
                totalSets = sets.size
            )
        }.filter { point -> point.sessionDate >= (routineCreatedAt ?: 0L) }

    return ExerciseProgressSeries(
        exerciseId = exerciseId,
        exerciseName = exerciseName,
        dataPoints = points
    )
}