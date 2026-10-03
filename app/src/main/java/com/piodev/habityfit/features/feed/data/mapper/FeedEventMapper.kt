package com.piodev.habityfit.features.feed.data.mapper

import com.piodev.database.entities.FeedEventEntity
import com.piodev.database.model.SyncStatus
import com.piodev.habityfit.features.feed.domain.model.FeedEvent
import java.util.UUID

fun FeedEventEntity.toDomain() = FeedEvent(
    id = this.ID,
    userId = this.userId,
    eventType = this.eventType,
    exerciseId = this.exerciseId,
    metricValue = this.metricValue,
    customText = this.customText,
    createdAt = this.CREATED_AT
)

fun FeedEvent.toEntity(): FeedEventEntity = FeedEventEntity(
    userId = this.userId,
    eventType = this.eventType,
    exerciseId = this.exerciseId,
    metricValue = this.metricValue,
    customText = this.customText,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = this.createdAt.takeIf { it > 0L } ?: System.currentTimeMillis(),
    UPDATED_AT = System.currentTimeMillis(),
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)