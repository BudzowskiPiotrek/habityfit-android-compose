package com.piodev.habityfit.features.community.data.repository


import com.piodev.database.dao.UserDao
import com.piodev.database.dao.UserInvitationDao
import com.piodev.database.dao.UserRelationDao
import com.piodev.database.model.RelationStatus
import com.piodev.habityfit.features.community.data.mapper.createInvitationEntity
import com.piodev.habityfit.features.community.data.mapper.toDomainList
import com.piodev.habityfit.features.community.data.mapper.toEntity
import com.piodev.habityfit.features.community.domain.model.UserRelationCreate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import com.piodev.habityfit.features.community.domain.model.UserRelations
import com.piodev.habityfit.features.community.domain.repository.FriendsRepository

data class FriendsRepositoryImpl @Inject constructor(
    private val userRelationDao: UserRelationDao,
    private val userDao: UserDao,
    private val userInvitationDao: UserInvitationDao,
) : FriendsRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getFriends(userId: String): Flow<List<UserRelations>> {

        return userRelationDao.getAcceptedRelationsWithUser(userId)
            .map { entities -> entities.toDomainList(userId) }
    }


    override fun getPendingRequests(userId: String): Flow<List<UserRelations>> {
        return userRelationDao.getPendingRequestsWithUser(userId)
            .map { entities -> entities.toDomainList(userId) }
    }

    override fun getSentRequests(userId: String): Flow<List<UserRelations>> {
        return userRelationDao.getPendingRequestsWithFriend(userId)
            .map { entities -> entities.toDomainList(userId) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ENVIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun sendFriendRequest(friendCode: String, userId: String): Result<Unit> {
        return try {
            val rejected = userRelationDao.getRejectedRelation(userId, friendCode)
            if (rejected != null) {
                val elapsed =
                    System.currentTimeMillis() - (rejected.UPDATED_AT ?: rejected.CREATED_AT)
                val cooldownMs = 72 * 60 * 60 * 1000L
                if (elapsed < cooldownMs) {
                    val remaining = cooldownMs - elapsed
                    val hoursLeft = remaining / (60 * 60 * 1000)
                    val minutesLeft = (remaining % (60 * 60 * 1000)) / (60 * 1000)
                    return Result.failure(Exception("Espera ${hoursLeft}h ${minutesLeft}m antes de reintentar"))
                }
                userRelationDao.reactivateRelation(
                    rejected.ID,
                    RelationStatus.PENDING,
                    System.currentTimeMillis()
                )
                return Result.success(Unit)
            }

            if (userRelationDao.existsRelation(userId, friendCode)) {
                return Result.failure(Exception("Ya existe una relación o solicitud pendiente."))
            }

            val now = System.currentTimeMillis()
            val newRelationDomain = UserRelationCreate(
                id = UUID.randomUUID().toString(),
                createdAt = now,
                updatedAt = now,
                userId = userId,
                friendId = friendCode,
                status = RelationStatus.PENDING
            )
            userRelationDao.insert(newRelationDomain.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun acceptFriendRequest(relationId: String): Result<Unit> {
        return try {
            val now = System.currentTimeMillis()
            userRelationDao.updateRelationStatus(relationId, RelationStatus.ACCEPTED, now)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun declineFriendRequest(relationId: String): Result<Unit> {
        return try {
            val now = System.currentTimeMillis()
            userRelationDao.updateRelationStatus(relationId, RelationStatus.REJECTED, now)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendInvitation(email: String): Result<Unit> {
        return try {
            userInvitationDao.insert(createInvitationEntity(email))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}