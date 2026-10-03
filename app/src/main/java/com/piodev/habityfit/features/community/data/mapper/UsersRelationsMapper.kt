package com.piodev.habityfit.features.community.data.mapper

import com.piodev.database.entities.UserRelationEntity
import com.piodev.database.model.SyncStatus
import com.piodev.database.model.UserRelationWithUser
import com.piodev.habityfit.features.community.domain.model.User
import com.piodev.habityfit.features.community.domain.model.UserRelationCreate
import com.piodev.habityfit.features.community.domain.model.UserRelations


fun UserRelationWithUser.toDomain(currentUserId: String): UserRelations {
    val targetUserEntity = if (relation.userId == currentUserId) {
        friend
    } else {
        user
    }
    return UserRelations(
        id = relation.ID,
        createdAt = relation.CREATED_AT,
        friend = targetUserEntity?.toDomain() ?: User(
            id = if (relation.userId == currentUserId) relation.friendId else relation.userId,
            fullName = "",
            email = "",
            sex = null,
            height = null,
            birthDate = null
        ),
        status = relation.status
    )
}

fun List<UserRelationWithUser>.toDomainList(currentUserId: String): List<UserRelations> {
    return this.map { it.toDomain(currentUserId) }
}

fun UserRelationCreate.toEntity(): UserRelationEntity {
    return UserRelationEntity(
        ID = this.id,
        CREATED_AT = this.createdAt,
        UPDATED_AT = this.updatedAt,
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        userId = this.userId,
        friendId = this.friendId,
        status = this.status
    )
}