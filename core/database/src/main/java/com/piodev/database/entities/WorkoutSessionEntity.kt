package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.SyncStatus

@Entity(
    tableName = "WORKOUT_SESSIONS",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["ID"],
            childColumns = ["USER_ID"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["USER_ID"])]
)
data class WorkoutSessionEntity(

    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "USER_ID") val userId: String,

    @ColumnInfo(name = "DATE") val date: Long,

    @ColumnInfo(name = "DURATION") val durationMinutes: Int,

    @ColumnInfo(name = "NOTES") val notes: String?
) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)