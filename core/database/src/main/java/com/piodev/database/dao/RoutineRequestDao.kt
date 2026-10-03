package com.piodev.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.piodev.database.entities.RoutineRequestEntity
import com.piodev.database.model.RequestStatus
import com.piodev.database.model.RoutineRequestWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineRequestDao : BaseDao<RoutineRequestEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM ROUTINE_REQUESTS WHERE RECEIVER_ID = :userId AND DELETED_AT IS NULL")
    fun getReceivedRequests(userId: String): Flow<List<RoutineRequestEntity>>

    @Query("SELECT * FROM ROUTINE_REQUESTS WHERE RECEIVER_ID = :userId AND STATUS = 'PENDING' AND DELETED_AT IS NULL")
    fun getPendingReceived(userId: String): Flow<List<RoutineRequestEntity>>

    @Transaction
    @Query("SELECT * FROM ROUTINE_REQUESTS WHERE RECEIVER_ID = :userId AND STATUS = 'PENDING' AND DELETED_AT IS NULL")
    fun getPendingReceivedWithDetails(userId: String): Flow<List<RoutineRequestWithDetails>>

    @Query("SELECT * FROM ROUTINE_REQUESTS WHERE SENDER_ID = :userId AND DELETED_AT IS NULL")
    fun getSentRequests(userId: String): Flow<List<RoutineRequestEntity>>

    @Query("SELECT * FROM ROUTINE_REQUESTS WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<RoutineRequestEntity>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE ROUTINE_REQUESTS SET STATUS = :newStatus, SYNC_STATUS = 'PENDING', UPDATED_AT = :updatedAt WHERE ID = :requestId")
    suspend fun updateRequestStatus(
        requestId: String,
        newStatus: RequestStatus,
        updatedAt: Long
    )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE ROUTINE_REQUESTS SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE ROUTINE_REQUESTS SET DELETED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)
}