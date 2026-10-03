package com.piodev.habityfit.features.graphics.data.repository

import com.piodev.database.dao.RoutineDao
import com.piodev.database.dao.WorkoutDao
import com.piodev.network.dto.common.SessionManager
import com.piodev.habityfit.features.graphics.data.mapper.toProgressSeries
import com.piodev.habityfit.features.graphics.domain.model.ExerciseProgressSeries
import com.piodev.habityfit.features.graphics.domain.repository.GraphicsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GraphicsRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val routineDao: RoutineDao,
    private val sessionManager: SessionManager
) : GraphicsRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA DE DATOS PARA LAS GRÁFICAS DE PROGRESO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getProgressSeriesForRoutine(routineId: String): Flow<List<ExerciseProgressSeries>> {
        val userId = sessionManager.getUserId() ?: return flowOf(emptyList())
        return combine(
            routineDao.getRoutineWithExercises(routineId),
            workoutDao.getWorkoutHistoryAsc(userId)
        ) { routineWithEx, allSessions ->
            val routine = routineWithEx?.routine ?: return@combine emptyList()
            val routineCreatedAt = routine.CREATED_AT

            val exercises = routineWithEx.exerciseConfigs
                ?.filter { it.config.DELETED_AT == null }
                ?: return@combine emptyList()

            exercises.map { item ->
                allSessions.toProgressSeries(
                    exerciseId = item.config.exerciseId,
                    exerciseName = item.exercise.name,
                    routineCreatedAt = routineCreatedAt
                )
            }
        }
    }

    override fun getProgressSeriesForExercise(exerciseId: String): Flow<ExerciseProgressSeries?> {
        val userId = sessionManager.getUserId() ?: return flowOf(null)
        return workoutDao.getWorkoutHistoryAsc(userId).map { sessions ->
            val name = sessions
                .flatMap { it.exerciseConfigs }
                .firstOrNull { it.config.exerciseId == exerciseId }
                ?.exercise?.name ?: return@map null
            sessions.toProgressSeries(
                exerciseId,
                name
            )
        }
    }
}