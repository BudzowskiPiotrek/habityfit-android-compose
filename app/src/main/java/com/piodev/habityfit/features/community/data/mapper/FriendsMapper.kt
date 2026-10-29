package com.piodev.habityfit.features.community.data.mapper

import com.piodev.database.entities.UserInvitationEntity
import com.piodev.database.model.InvitationStatus
import com.piodev.database.model.SyncStatus
import java.util.UUID

fun createInvitationEntity(email: String): UserInvitationEntity {
    val now = System.currentTimeMillis()
    return UserInvitationEntity(
        ID = UUID.randomUUID().toString(),
        CREATED_AT = now,
        UPDATED_AT = now,
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        email = email,
        token = null,
        senderUserId = "",
        expiresAt = null,
        status = InvitationStatus.PENDING,
        sended = false
    )
}