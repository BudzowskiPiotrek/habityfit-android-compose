package com.piodev.data.mapper

import com.piodev.data.model.FeedEvent
import com.piodev.database.entities.FeedEventEntity
import com.piodev.database.model.SyncStatus
import com.piodev.network.dto.sync.FeedEventDto
import java.util.Date
import java.util.UUID

// --- FEED EVENT ---

fun FeedEventEntity.toDomain() = FeedEvent(
    id = this.ID,
    userId = this.userId,
    eventType = this.eventType,
    exerciseId = this.exerciseId,
    metricValue = this.metricValue,
    customText = this.customText
)

fun FeedEvent.toEntity(): FeedEventEntity = FeedEventEntity(
    userId = this.userId ,
    eventType = this.eventType,
    exerciseId = this.exerciseId,
    metricValue = this.metricValue,
    customText = this.customText,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun FeedEventEntity.toDto() = FeedEventDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    userId = this.userId,
    eventType = this.eventType,
    exerciseId = this.exerciseId,
    metricValue = this.metricValue,
    customText = this.customText
)

fun FeedEventDto.toEntity(): FeedEventEntity = FeedEventEntity(
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    userId = this.userId,
    eventType = this.eventType,
    exerciseId = this.exerciseId,
    metricValue = this.metricValue,
    customText = this.customText,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)