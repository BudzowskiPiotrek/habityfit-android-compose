package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM USERS WHERE ID = :id")
    fun getUserByIdFlow(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM USERS WHERE ID = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM USERS WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<UserEntity>

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR (SOFT DELETE)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE USERS SET DELETED_AT = :date, UPDATED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE USERS SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE USERS SET LAST_SYNC_DATE = :timestamp WHERE ID = :userId")
    suspend fun updateLastSyncDate(userId: String, timestamp: Long)
}