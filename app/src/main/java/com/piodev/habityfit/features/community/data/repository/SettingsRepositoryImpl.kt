package com.piodev.habityfit.features.community.data.repository

import com.piodev.database.dao.UserDao
import com.piodev.database.model.SyncStatus
import com.piodev.habityfit.features.community.data.mapper.toDomain
import com.piodev.habityfit.features.community.data.mapper.toEntity
import com.piodev.habityfit.features.community.domain.model.User
import com.piodev.habityfit.features.community.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
var userDao: UserDao
) : SettingsRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getUser(userId: String): Flow<User?> {
        return userDao.getUserByIdFlow(userId).map { entity ->
            entity?.toDomain()
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun updateProfile(user: User): Result<Unit> {
        return try {
            val existingEntity = userDao.getUserById(user.id)
            val updatedEntity = user.toEntity(existingEntity)
            updatedEntity.UPDATED_AT = System.currentTimeMillis()
            updatedEntity.SYNC_STATUS = SyncStatus.PENDING
            userDao.insert(updatedEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun deactivateAccount(userId: String): Result<Unit> {
        return try {
            val now = System.currentTimeMillis()
            userDao.softDeleteById(userId, now)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}