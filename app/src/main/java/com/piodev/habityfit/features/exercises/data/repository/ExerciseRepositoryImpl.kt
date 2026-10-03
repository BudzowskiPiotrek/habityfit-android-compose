package com.piodev.habityfit.features.exercises.data.repository

import com.piodev.database.dao.ExerciseDao
import com.piodev.habityfit.features.exercises.data.mapper.toDomain
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDao
) : ExerciseRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getExercises(): Flow<List<Exercise>> {
        return dao.getExercisesWithMuscleGroups()
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getExercisesByMuscleGroup(muscleGroupId: String): Flow<List<Exercise>> {
        return dao.getExercisesByMuscleGroup(muscleGroupId)
            .map { list -> list.map { it.toDomain() } }
    }
}

