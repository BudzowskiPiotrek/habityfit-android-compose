package com.piodev.habityfit.features.community.data.mapper

import com.piodev.database.entities.UserEntity
import com.piodev.database.model.SyncStatus
import com.piodev.habityfit.features.community.domain.model.User


fun UserEntity.toDomain(): User {
    return User(
        id = this.ID,
        email = this.email,
        fullName = this.fullName,
        sex = this.sex,
        birthDate = this.birthDate,
        height = this.height,
    )
}

fun User.toEntity(
    existingEntity: UserEntity? = null
): UserEntity {
    return UserEntity(
        ID = this.id.ifBlank {
            existingEntity?.ID ?: throw IllegalArgumentException("User ID cannot be empty")
        },
        CREATED_AT = existingEntity?.CREATED_AT ?: System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        email = this.email,
        fullName = this.fullName,
        sex = this.sex,
        birthDate = this.birthDate,
        height = this.height,
        passwordHash = null,
        termsAcceptedAt = existingEntity?.termsAcceptedAt
    )
}
