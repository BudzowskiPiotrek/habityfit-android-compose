package com.piodev.data.model

import com.piodev.database.model.RelationStatus

data class UserRelation(
    val id: String,
    val userId: String,
    val friendId: String,
    val status: RelationStatus,
)
