package com.piodev.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.piodev.database.entities.RoutineEntity
import com.piodev.database.entities.RoutineRequestEntity
import com.piodev.database.entities.UserEntity

data class RoutineRequestWithDetails(
    @Embedded val request: RoutineRequestEntity,

    @Relation(
        parentColumn = "SENDER_ID",
        entityColumn = "ID",
        entity = UserEntity::class
    )
    val sender: UserEntity?,

    @Relation(
        parentColumn = "ROUTINE_ID",
        entityColumn = "ID",
        entity = RoutineEntity::class
    )
    val routine: RoutineEntity?
)