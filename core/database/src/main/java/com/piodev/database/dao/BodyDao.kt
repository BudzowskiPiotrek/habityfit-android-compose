package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.BodyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyDao : BaseDao<BodyEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM BODY_MEASUREMENTS WHERE ID = :id LIMIT 1")
    suspend fun getBodyById(id: String): BodyEntity?

    @Query("SELECT * FROM BODY_MEASUREMENTS WHERE ID_USER = :userId AND DELETED_AT IS NULL ORDER BY DATE DESC")
    fun getHistoryByUserId(userId: String): Flow<List<BodyEntity>>

    @Query("SELECT * FROM BODY_MEASUREMENTS WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<BodyEntity>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR (SOFT DELETE)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE BODY_MEASUREMENTS SET DELETED_AT = :date, UPDATED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE BODY_MEASUREMENTS SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)
}