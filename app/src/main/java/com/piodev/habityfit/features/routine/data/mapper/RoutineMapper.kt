package com.piodev.habityfit.features.routine.data.mapper

import com.piodev.database.entities.RoutineEntity
import com.piodev.database.entities.RoutineExerciseEntity
import com.piodev.database.entities.RoutineRequestEntity
import com.piodev.database.model.RequestStatus
import com.piodev.database.model.SyncStatus
import com.piodev.database.model.RoutineWithExercises
import com.piodev.habityfit.features.routine.domain.model.Routine
import com.piodev.habityfit.features.routine.domain.model.RoutineExercise
import com.piodev.habityfit.features.routine.domain.model.RoutineRequest
import java.util.UUID

fun RoutineEntity.toDomain(exercises: List<RoutineExercise> = emptyList()): Routine {
    return Routine(
        id = this.ID,
        name = this.name,
        description = this.description,
        exercises = exercises
    )
}

fun RoutineWithExercises?.toDomain(): Routine? {
    if (this == null) return null

    val exercises = this.exerciseConfigs
        .filter { item -> item.config.DELETED_AT == null }
        .map { item ->
        RoutineExercise(
            id = item.config.ID,
            exerciseId = item.config.exerciseId,
            name = item.exercise.name,
            sets = item.config.sets,
            reps = item.config.reps,
            order = item.config.order,
            restSecond = item.config.restSecond,
            targetWeight = item.config.targetWeight,
            imageUrl = item.exercise.imageUrl
        )
    }.sortedBy { it.order }

    return this.routine.toDomain(exercises)
}

fun Routine.toEntity(userId: String): RoutineEntity {
    return RoutineEntity(
        ID = this.id.ifBlank { UUID.randomUUID().toString() },
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        userId = userId,
        name = this.name,
        description = this.description,
    )
}

fun RoutineExercise.toEntity(routineId: String): RoutineExerciseEntity {
    return RoutineExerciseEntity(
        ID = this.id.ifBlank { UUID.randomUUID().toString() },
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        routineId = routineId,
        exerciseId = this.exerciseId,
        order = this.order,
        sets = this.sets,
        reps = this.reps,
        targetWeight = this.targetWeight,
        restSecond = this.restSecond,
    )
}

fun RoutineExerciseEntity.toDomain(): RoutineExercise {
    return RoutineExercise(
        id = this.ID,
        exerciseId = this.exerciseId,
        name = "",
        sets = this.sets,
        reps = this.reps,
        order = this.order,
        restSecond = this.restSecond,
        targetWeight = this.targetWeight
    )
}

fun RoutineRequest.toEntity(senderId: String): RoutineRequestEntity {
    return RoutineRequestEntity(
        ID = UUID.randomUUID().toString(),
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        senderId = senderId,
        receiverId = this.targetUserId,
        routineId = this.routineId,
        status = RequestStatus.PENDING
    )
}