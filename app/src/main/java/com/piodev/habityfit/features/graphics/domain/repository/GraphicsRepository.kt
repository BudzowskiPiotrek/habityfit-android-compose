package com.piodev.habityfit.features.graphics.domain.repository

import com.piodev.habityfit.features.graphics.domain.model.ExerciseProgressSeries
import kotlinx.coroutines.flow.Flow

interface GraphicsRepository {
    fun getProgressSeriesForRoutine(routineId: String): Flow<List<ExerciseProgressSeries>>
    fun getProgressSeriesForExercise(exerciseId: String): Flow<ExerciseProgressSeries?>
}