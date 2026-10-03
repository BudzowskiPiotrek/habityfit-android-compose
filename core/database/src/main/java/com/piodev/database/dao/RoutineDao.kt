package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.RoutineEntity
import com.piodev.database.entities.RoutineExerciseEntity
import com.piodev.database.model.RoutineWithExercises
import com.piodev.database.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao : BaseDao<RoutineEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM ROUTINES WHERE ID = :id LIMIT 1")
    suspend fun getRoutineById(id: String): RoutineEntity?

    @Query("SELECT * FROM ROUTINES WHERE USER_ID = :userId AND DELETED_AT IS NULL ORDER BY CREATED_AT DESC")
    fun getRoutinesByUser(userId: String): Flow<List<RoutineEntity>>

    @Transaction
    @Query("SELECT * FROM ROUTINES WHERE ID = :routineId AND DELETED_AT IS NULL")
    fun getRoutineWithExercises(routineId: String): Flow<RoutineWithExercises?>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineExercise(relation: RoutineExerciseEntity)

    @Query("UPDATE ROUTINES SET NAME = :name, DESCRIPTION = :description, UPDATED_AT = :date, SYNC_STATUS = :sync WHERE ID = :id")
    suspend fun updateRoutineDetails(
        id: String,
        name: String,
        description:
        String?,
        sync: SyncStatus,
        date: Long
    )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR (SOFT DELETE)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE ROUTINES SET DELETED_AT = :date, UPDATED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM ROUTINES WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<RoutineEntity>

    @Query("UPDATE ROUTINES SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)
}