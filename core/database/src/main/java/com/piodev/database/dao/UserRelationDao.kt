package com.piodev.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.piodev.database.entities.UserRelationEntity
import com.piodev.database.model.RelationStatus
import com.piodev.database.model.UserRelationWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRelationDao : BaseDao<UserRelationEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Transaction
    @Query("SELECT * FROM USER_RELATIONS WHERE (USER_ID = :userId OR FRIEND_ID = :userId) AND STATUS = 'ACCEPTED' AND DELETED_AT IS NULL")
    fun getAcceptedRelationsWithUser(userId: String): Flow<List<UserRelationWithUser>>

    @Transaction
    @Query("SELECT * FROM USER_RELATIONS WHERE FRIEND_ID = :userId AND STATUS = 'PENDING' AND DELETED_AT IS NULL")
    fun getPendingRequestsWithUser(userId: String): Flow<List<UserRelationWithUser>>

    @Transaction
    @Query("SELECT * FROM USER_RELATIONS WHERE USER_ID = :userId AND STATUS = 'PENDING' AND DELETED_AT IS NULL")
    fun getPendingRequestsWithFriend(userId: String): Flow<List<UserRelationWithUser>>

    @Query("SELECT * FROM USER_RELATIONS WHERE ((USER_ID = :userId AND FRIEND_ID = :friendId) OR (USER_ID = :friendId AND FRIEND_ID = :userId)) AND STATUS = 'REJECTED' AND DELETED_AT IS NULL LIMIT 1")
    suspend fun getRejectedRelation(userId: String, friendId: String): UserRelationEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM USER_RELATIONS WHERE ((USER_ID = :userId AND FRIEND_ID = :friendId) OR (USER_ID = :friendId AND FRIEND_ID = :userId)) AND DELETED_AT IS NULL)")
    suspend fun existsRelation(userId: String, friendId: String): Boolean

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE USER_RELATIONS SET STATUS = :newStatus, SYNC_STATUS = 'PENDING', UPDATED_AT = :updatedAt WHERE ID = :relationId")
    suspend fun updateRelationStatus(relationId: String, newStatus: RelationStatus, updatedAt: Long)

    @Query("UPDATE USER_RELATIONS SET STATUS = :newStatus, SYNC_STATUS = 'PENDING', UPDATED_AT = :updatedAt WHERE ID = :relationId")
    suspend fun reactivateRelation(relationId: String, newStatus: RelationStatus, updatedAt: Long)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM USER_RELATIONS WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<UserRelationEntity>

    @Query("UPDATE USER_RELATIONS SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE USER_RELATIONS SET DELETED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)
}