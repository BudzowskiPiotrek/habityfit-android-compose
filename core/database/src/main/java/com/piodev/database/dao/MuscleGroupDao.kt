package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.MuscleGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MuscleGroupDao : BaseDao<MuscleGroupEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM MUSCLE_GROUP ORDER BY name ASC")
    fun getAllMuscleGroups(): Flow<List<MuscleGroupEntity>>

    @Query("SELECT * FROM MUSCLE_GROUP WHERE ID = :id LIMIT 1")
    suspend fun getMuscleGroupById(id: String): MuscleGroupEntity?

    /////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM MUSCLE_GROUP WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<MuscleGroupEntity>

    @Query("UPDATE MUSCLE_GROUP SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)
}