package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.RoutineExerciseEntity
import com.piodev.database.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineExerciseDao : BaseDao<RoutineExerciseEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM ROUTINE_Exercises WHERE ROUTINE_ID = :routineId ORDER BY SORT_ORDER ASC")
    fun getExercisesByRoutine(routineId: String): Flow<List<RoutineExerciseEntity>>

    @Query("SELECT * FROM ROUTINE_Exercises WHERE ID = :id")
    suspend fun getExerciseConfigById(id: String): RoutineExerciseEntity?

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM ROUTINE_Exercises WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<RoutineExerciseEntity>

    @Query("UPDATE ROUTINE_Exercises SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR (SOFT DELETE)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE ROUTINE_Exercises SET DELETED_AT = :date, UPDATED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE ROUTINE_Exercises SET SETS = :sets, REPS = :reps, REST_SECOND = :repsSeconds, TARGET_WEIGHT = :weight,  UPDATED_AT = :date, SYNC_STATUS = :sync WHERE ID = :id")
    suspend fun updateExerciseDetails(
        id: String,
        sets: Int,
        reps: Int,
        repsSeconds: Int,
        weight: Double?,
        sync: SyncStatus,
        date: Long
    )
}