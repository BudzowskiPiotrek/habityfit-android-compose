package com.piodev.habityfit.features.auth.data.mapper

import com.piodev.database.entities.UserEntity
import com.piodev.network.dto.response.LoginResponseDto
import com.piodev.habityfit.features.auth.domain.model.User

fun LoginResponseDto.toDomain(): User {
    return User(
        id = this.user.id,
        email = this.user.email,
        name = this.user.fullName,
        termsAcceptedAt = this.user.termsAcceptedAt?.time
    )
}
fun UserEntity.toDomain(): User {
    return User(
        id = this.ID,
        email = this.email,
        name = this.fullName,
        termsAcceptedAt = this.termsAcceptedAt
    )
}