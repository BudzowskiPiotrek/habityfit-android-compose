package com.piodev.habityfit.features.feed.data.repository

import com.piodev.database.dao.FeedEventDao
import com.piodev.database.model.FeedEventType
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.feed.data.mapper.toDomain
import com.piodev.habityfit.features.feed.data.mapper.toEntity
import com.piodev.habityfit.features.feed.domain.model.FeedEvent
import com.piodev.habityfit.features.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedEventDao: FeedEventDao,
    private val authRepository: AuthRepository
) : FeedRepository {

    override fun getFeedEvents(): Flow<List<FeedEvent>> {
        return authRepository.getCurrentUser().flatMapLatest { user ->
            val userId = user?.id
            val threshold = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)
            feedEventDao.getAllFeedEvents().map { entities ->
                entities.map { it.toDomain() }
                    .filter { it.createdAt >= threshold && it.userId != userId }
            }
        }
    }

    override suspend fun createFeedEvent(
        userId: String,
        eventType: FeedEventType,
        customText: String?,
        exerciseId: String
    ): Result<Unit> {
        return try {
            val event = FeedEvent(
                id = UUID.randomUUID().toString(),
                userId = userId,
                eventType = eventType,
                exerciseId = exerciseId,
                customText = customText
            )
            feedEventDao.insert(event.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cleanOldFeedEvents() {
        val threshold = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)
        feedEventDao.deleteOlderThan(threshold)
    }
}