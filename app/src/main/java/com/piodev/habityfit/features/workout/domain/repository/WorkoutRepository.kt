package com.piodev.habityfit.features.workout.domain.repository

import com.piodev.database.entities.ExerciseEntity
import com.piodev.habityfit.features.workout.domain.model.WorkoutSession
import com.piodev.habityfit.features.workout.domain.model.WorkoutSet
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun getWorkoutById(id: String): WorkoutSession?
    fun getSessions(userId: String): Flow<List<WorkoutSession>>
    fun getSessionWithExercises(sessionId: String): Flow<WorkoutSession?>
    fun getSetsForExercise(workoutExerciseId: String): Flow<List<WorkoutSet>>
    suspend fun getRestSecondsForExercise(workoutExerciseId: String): Int
    suspend fun getExerciseByWorkoutExerciseId(workoutExerciseId: String): ExerciseEntity?

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun updateSet(workoutExerciseId: String, set: WorkoutSet)
    suspend fun saveWorkoutSession(
        userId: String,
        routineId: String,
        session: WorkoutSession
    )

    suspend fun updateWorkoutDetails(id: String, date: Long, duration: Int, notes: String?)
    suspend fun  updateWorkoutDuration(sessionId: String, duration: Int)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun deleteWorkoutSession(sessionId: String)
}