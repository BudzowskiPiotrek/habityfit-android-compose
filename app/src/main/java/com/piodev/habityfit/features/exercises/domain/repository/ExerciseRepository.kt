package com.piodev.habityfit.features.exercises.domain.repository

import com.piodev.habityfit.features.exercises.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun getExercises(): Flow<List<Exercise>>
    fun getExercisesByMuscleGroup(muscleGroupId: String): Flow<List<Exercise>>
}