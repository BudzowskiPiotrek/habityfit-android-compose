package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.RequestStatus
import com.piodev.database.model.SyncStatus

@Entity(
    tableName = "ROUTINE_REQUESTS",
    foreignKeys = [    ],
    indices = [
        Index(value = ["SENDER_ID"]),
        Index(value = ["RECEIVER_ID"])
    ]
)
data class RoutineRequestEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "SENDER_ID") val senderId: String,

    @ColumnInfo(name = "RECEIVER_ID") val receiverId: String,

    @ColumnInfo(name = "ROUTINE_ID") val routineId: String,

    @ColumnInfo(name = "STATUS") val status: RequestStatus

) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)



