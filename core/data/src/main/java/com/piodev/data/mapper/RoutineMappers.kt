package com.piodev.data.mapper

import com.piodev.data.model.Routine
import com.piodev.data.model.RoutineExercise
import com.piodev.data.model.RoutineRequest
import com.piodev.database.entities.RoutineEntity
import com.piodev.database.entities.RoutineExerciseEntity
import com.piodev.database.entities.RoutineRequestEntity
import com.piodev.database.model.SyncStatus
import com.piodev.network.dto.sync.RoutineExerciseDto
import com.piodev.network.dto.sync.RoutineDto
import com.piodev.network.dto.sync.RoutineRequestDto
import java.util.UUID
import java.util.Date

// --- ROUTINE ---
fun RoutineDto.toDomain(exercises: List<RoutineExercise> = emptyList()) = Routine(
    id = this.id,
    userId = this.userId,
    name = this.name,
    description = this.description,
    exercises = exercises
)

fun RoutineEntity.toDomain(exercises: List<RoutineExercise> = emptyList()) = Routine(
    id = this.ID,
    userId = this.userId,
    name = this.name,
    description = this.description,
    exercises = exercises
)

fun Routine.toEntity(): RoutineEntity = RoutineEntity(
    userId = this.userId,
    name = this.name,
    description = this.description,

    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun RoutineEntity.toDto() = RoutineDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    userId = this.userId,
    name = this.name,
    description = this.description
)

fun RoutineDto.toEntity(): RoutineEntity = RoutineEntity(
    userId = this.userId,
    name = this.name,
    description = this.description,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- ROUTINE REQUEST ---

fun RoutineRequestDto.toDomain() = RoutineRequest(
    id = this.id,
    senderId = this.senderId,
    receiverId = this.receiverId,
    routineId = this.routineId,
    status = this.status
)

fun RoutineRequestEntity.toDomain() = RoutineRequest(
    id = this.ID,
    senderId = this.senderId,
    receiverId = this.receiverId,
    routineId = this.routineId,
    status = this.status
)

fun RoutineRequest.toEntity(): RoutineRequestEntity = RoutineRequestEntity(
    senderId = this.senderId,
    receiverId = this.receiverId,
    routineId = this.routineId,
    status = this.status,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun RoutineRequestEntity.toDto() = RoutineRequestDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    senderId = this.senderId,
    receiverId = this.receiverId,
    routineId = this.routineId,
    status = this.status
)

fun RoutineRequestDto.toEntity(): RoutineRequestEntity = RoutineRequestEntity(
    senderId = this.senderId,
    receiverId = this.receiverId,
    routineId = this.routineId,
    status = this.status,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- ROUTINE EXERCISE ---

fun RoutineExerciseDto.toDomain() = RoutineExercise(
    id = this.id,
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    order = this.order,
    sets = this.sets,
    reps = this.reps,
    targetWeight = this.targetWeight,
    restSecond = this.restSecond
)

fun RoutineExerciseEntity.toDomain() = RoutineExercise(
    id = this.ID,
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    order = this.order,
    sets = this.sets,
    reps = this.reps,
    targetWeight = this.targetWeight,
    restSecond = this.restSecond
)

fun RoutineExercise.toEntity(): RoutineExerciseEntity = RoutineExerciseEntity(
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    order = this.order,
    sets = this.sets,
    reps = this.reps,
    targetWeight = this.targetWeight,
    restSecond = this.restSecond,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun RoutineExerciseEntity.toDto() = RoutineExerciseDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    order = this.order,
    sets = this.sets,
    reps = this.reps,
    targetWeight = this.targetWeight,
    restSecond = this.restSecond
)

fun RoutineExerciseDto.toEntity(): RoutineExerciseEntity = RoutineExerciseEntity(
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    order = this.order,
    sets = this.sets,
    reps = this.reps,
    targetWeight = this.targetWeight,
    restSecond = this.restSecond,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)