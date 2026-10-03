package com.piodev.data.mapper

import com.piodev.data.model.Exercise
import com.piodev.data.model.MuscleGroup
import com.piodev.database.entities.ExerciseEntity
import com.piodev.database.entities.MuscleGroupEntity
import com.piodev.database.entities.ExerciseMuscleGroupEntity
import com.piodev.database.model.SyncStatus
import com.piodev.network.dto.sync.ExerciseDto
import com.piodev.network.dto.sync.MuscleGroupDto
import com.piodev.network.dto.sync.ExerciseMuscleGroupDto
import java.util.UUID
import java.util.Date

// --- MUSCLE GROUP ---

fun MuscleGroupDto.toDomain() = MuscleGroup(
    id = this.id,
    name = this.name,
    description = this.description,
    minSets = this.minSets,
    maxSets = this.maxSets
)

fun MuscleGroupEntity.toDomain() = MuscleGroup(
    id = this.ID,
    name = this.name,
    description = this.description,
    minSets = this.minSets,
    maxSets = this.maxSets
)

fun MuscleGroup.toEntity(): MuscleGroupEntity = MuscleGroupEntity(
    name = this.name,
    description = this.description,
    minSets = this.minSets,
    maxSets = this.maxSets,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun MuscleGroupEntity.toDto() = MuscleGroupDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    name = this.name,
    description = this.description,
    minSets = this.minSets,
    maxSets = this.maxSets
)

fun MuscleGroupDto.toEntity(): MuscleGroupEntity = MuscleGroupEntity(
    name = this.name,
    description = this.description,
    minSets = this.minSets,
    maxSets = this.maxSets,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- EXERCISE ---

fun ExerciseDto.toDomain() = Exercise(
    id = this.id,
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    equipmentType = this.equipmentType
)

fun ExerciseEntity.toDomain(muscleGroups: List<MuscleGroup> = emptyList()) = Exercise(
    id = this.ID,
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    equipmentType = this.equipmentType,
    muscleGroups = muscleGroups
)


fun Exercise.toEntity(): ExerciseEntity = ExerciseEntity(
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    equipmentType = this.equipmentType,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun ExerciseEntity.toDto() = ExerciseDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    equipmentType = this.equipmentType,
)

fun ExerciseDto.toEntity(): ExerciseEntity = ExerciseEntity(
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    equipmentType = this.equipmentType,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- EXERCISE <-> MUSCLE RELATION ---

fun ExerciseMuscleGroupEntity.toDto() = ExerciseMuscleGroupDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    exerciseId = this.exerciseId,
    muscleId = this.muscleId
)

fun ExerciseMuscleGroupDto.toEntity(): ExerciseMuscleGroupEntity = ExerciseMuscleGroupEntity(
    exerciseId = this.exerciseId,
    muscleId = this.muscleId,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)
