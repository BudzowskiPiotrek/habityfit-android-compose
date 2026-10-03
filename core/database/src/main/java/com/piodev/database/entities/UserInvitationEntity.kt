package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.InvitationStatus
import com.piodev.database.model.SyncStatus


@Entity(
    tableName = "USER_INVITATION",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["ID"],
            childColumns = ["SENDER_USER_ID"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["SENDER_USER_ID"]),
    ]
)
data class UserInvitationEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "EMAIL") val email: String,

    @ColumnInfo(name = "TOKEN") val token: String?,

    @ColumnInfo(name = "SENDER_USER_ID") val senderUserId: String,

    @ColumnInfo(name = "EXPIRES_AT") val expiresAt: Long?,

    @ColumnInfo(name = "STATUS") val status: InvitationStatus,

    @ColumnInfo(name = "SENDED") val sended: Boolean
    ) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)
