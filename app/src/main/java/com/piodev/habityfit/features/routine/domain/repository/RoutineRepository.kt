package com.piodev.habityfit.features.routine.domain.repository

import com.piodev.habityfit.features.routine.domain.model.Routine
import com.piodev.habityfit.features.routine.domain.model.RoutineExercise
import com.piodev.habityfit.features.routine.domain.model.RoutineRequest
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun getRoutines(userId: String): Flow<List<Routine>>

    suspend fun getRoutineById(id: String): Routine?
    suspend fun getExerciseById(id: String): RoutineExercise?
    fun getRoutineWithExercises(routineId: String): Flow<Routine?>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun saveRoutineWithExercises(
        routine: Routine,
        exercises: List<RoutineExercise>,
        userId: String
    )

    suspend fun saveRoutineExercise(exercise: RoutineExercise, routineId: String)

    suspend fun updateRoutineDetails(
        id: String,
        name: String,
        description: String?
    )

    suspend fun updateExerciseConfiguration(
        id: String,
        sets: Int,
        reps: Int,
        repsSeconds: Int,
        weight: Double?
    )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun deleteRoutine(id: String)
    suspend fun deleteExerciseFromRoutine(id: String)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RUTINE REQUESTS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun sendRoutineRequest(request: RoutineRequest, senderId: String)
}