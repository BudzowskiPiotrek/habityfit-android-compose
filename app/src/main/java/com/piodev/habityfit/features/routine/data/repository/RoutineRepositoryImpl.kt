package com.piodev.habityfit.features.routine.data.repository

import android.util.Log
import com.piodev.database.dao.RoutineDao
import com.piodev.database.dao.RoutineExerciseDao
import com.piodev.database.dao.RoutineRequestDao
import com.piodev.database.model.SyncStatus
import com.piodev.habityfit.features.routine.data.mapper.toDomain
import com.piodev.habityfit.features.routine.data.mapper.toEntity
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.routine.domain.model.Routine
import com.piodev.habityfit.features.routine.domain.model.RoutineExercise
import com.piodev.habityfit.features.routine.domain.model.RoutineRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val dao: RoutineDao,
    private val exerciseDao: RoutineExerciseDao,
    private val routineRequestDao: RoutineRequestDao,
) : RoutineRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getRoutines(userId: String): Flow<List<Routine>> {
        return dao.getRoutinesByUser(userId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getRoutineById(id: String): Routine? {
        return dao.getRoutineById(id)?.toDomain()
    }

    override suspend fun getExerciseById(id: String): RoutineExercise? {
        return exerciseDao.getExerciseConfigById(id)?.toDomain()
    }

    override fun getRoutineWithExercises(routineId: String): Flow<Routine?> {
        return dao.getRoutineWithExercises(routineId).map { it -> it.toDomain() }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun saveRoutineWithExercises(
        routine: Routine,
        exercises: List<RoutineExercise>,
        userId: String
    ) {
        val routineEntity = routine.toEntity(userId)
        dao.insert(routineEntity)

        val relationEntities = exercises.map { it.toEntity(routineEntity.ID) }
        relationEntities.forEach { relation ->
            dao.insertRoutineExercise(relation)
        }
    }

    override suspend fun saveRoutineExercise(exercise: RoutineExercise, routineId: String) {
        val entity = exercise.toEntity(routineId)
        dao.insertRoutineExercise(entity)
    }

    override suspend fun updateRoutineDetails(
        id: String,
        name: String,
        description: String?
    ) {
        dao.updateRoutineDetails(
            id,
            name,
            description,
            SyncStatus.PENDING,
            System.currentTimeMillis()
        )
    }

    // TO DO ( EN FUTURO PODER EDITAR ORDEN DE LOS EJERCICIOS O AÑADIR )
    override suspend fun updateExerciseConfiguration(
        id: String,
        sets: Int,
        reps: Int,
        repsSeconds: Int,
        weight: Double?
    ) {
        Log.d("RoutineRepositoryImpl", "Updating exercise configuration: $id")
        exerciseDao.updateExerciseDetails(
            id,
            sets,
            reps,
            repsSeconds,
            weight,
            SyncStatus.PENDING,
            date = System.currentTimeMillis()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun deleteRoutine(id: String) {
        dao.softDeleteById(
            id,
            date = System.currentTimeMillis()
        )
    }

    // TO DO ( CUNDO SE BORRE ORDENARLO PARA QUE NO HAYA ESPACIOS EN EL ORDEN DE LOS EJERCICIOS )
    override suspend fun deleteExerciseFromRoutine(id: String) {
        exerciseDao.softDeleteById(
            id,
            date = System.currentTimeMillis()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // COMPARTIR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun sendRoutineRequest(request: RoutineRequest, senderId: String) {
        routineRequestDao.insert(request.toEntity(senderId))
    }
}