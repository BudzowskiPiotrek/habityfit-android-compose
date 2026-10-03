package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.FeedEventType
import com.piodev.database.model.SyncStatus

@Entity(
    tableName = "FEED_EVENTS",
    indices = [
        Index(value = ["USER_ID"]),
        Index(value = ["EVENT_TYPE"])
    ]
)
data class FeedEventEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "USER_ID") val userId: String,

    @ColumnInfo(name = "EVENT_TYPE") val eventType: FeedEventType,

    @ColumnInfo(name = "EXERCISE_ID") val exerciseId: String? = null,

    @ColumnInfo(name = "METRIC_VALUE") val metricValue: Double? = null,

    @ColumnInfo(name = "CUSTOM_TEXT") val customText: String? = null,

    ) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)
