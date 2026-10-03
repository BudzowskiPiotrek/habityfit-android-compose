package com.piodev.habityfit.features.community.domain.model

import com.piodev.database.model.RelationStatus

data class UserRelations (
    val id: String,
    val createdAt: Long,
    val friend: User,
    val status: RelationStatus,
)

data class UserRelationCreate(
    val id: String,
    val createdAt: Long,
    val updatedAt: Long,
    val userId: String,
    val friendId: String,
    val status: RelationStatus,
)