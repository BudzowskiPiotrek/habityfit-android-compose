package com.piodev.habityfit.features.feed.domain.repository

import com.piodev.database.model.FeedEventType
import com.piodev.habityfit.features.feed.domain.model.FeedEvent
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getFeedEvents(): Flow<List<FeedEvent>>
    suspend fun createFeedEvent(userId: String, eventType: FeedEventType, customText: String?, exerciseId: String): Result<Unit>
    suspend fun cleanOldFeedEvents()
}