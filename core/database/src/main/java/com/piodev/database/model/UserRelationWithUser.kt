package com.piodev.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.piodev.database.entities.UserEntity
import com.piodev.database.entities.UserRelationEntity

class UserRelationWithUser(
    @Embedded val relation: UserRelationEntity,

    @Relation(
        parentColumn = "USER_ID",
        entityColumn = "ID",
        entity = UserEntity::class
    )
    val user: UserEntity,

    @Relation(
        parentColumn = "FRIEND_ID",
        entityColumn = "ID",
        entity = UserEntity::class
    )
    val friend: UserEntity?
)