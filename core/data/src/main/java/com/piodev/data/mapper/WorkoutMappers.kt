package com.piodev.data.mapper

import com.piodev.data.model.*
import com.piodev.database.entities.*
import com.piodev.database.model.SyncStatus
import com.piodev.network.dto.sync.*
import java.util.UUID
import java.util.Date

// --- WORKOUT SESSION ---

fun WorkoutSessionDto.toDomain(exercises: List<WorkoutExercise> = emptyList()) = WorkoutSession(
    id = this.id,
    date = this.date.time,
    durationMinutes = this.durationMinutes,
    notes = this.notes,
    exercises = exercises
)

fun WorkoutSessionEntity.toDomain(exercises: List<WorkoutExercise> = emptyList()) = WorkoutSession(
    id = this.ID,
    date = this.date,
    durationMinutes = this.durationMinutes,
    notes = this.notes,
    exercises = exercises
)

fun WorkoutSession.toEntity(userId: String): WorkoutSessionEntity = WorkoutSessionEntity(
    userId = userId,
    date = this.date,
    durationMinutes = this.durationMinutes,
    notes = this.notes,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun WorkoutSessionEntity.toDto() = WorkoutSessionDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    userId = userId,
    date = Date(this.date),
    durationMinutes = this.durationMinutes,
    notes = this.notes
)

fun WorkoutSessionDto.toEntity(userId: String): WorkoutSessionEntity = WorkoutSessionEntity(
    userId = userId,
    date = this.date.time,
    durationMinutes = this.durationMinutes,
    notes = this.notes,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- WORKOUT EXERCISE ---

fun WorkoutExerciseDto.toDomain(sets: List<ExerciseSet> = emptyList()) = WorkoutExercise(
    id = this.id,
    exerciseId = this.exerciseId,
    order = this.order,
    sets = sets,
    restSecond = this.restSecond
)

fun WorkoutExerciseEntity.toDomain(
    exerciseName: String = "",
    sets: List<ExerciseSet> = emptyList()
) = WorkoutExercise(
    id = this.ID,
    exerciseId = this.exerciseId,
    exerciseName = exerciseName,
    order = this.order,
    sets = sets,
    restSecond = this.restSecond
)

fun WorkoutExercise.toEntity(sessionId: String): WorkoutExerciseEntity = WorkoutExerciseEntity(
    sessionId = sessionId,
    exerciseId = this.exerciseId,
    order = this.order,
    restSecond = this.restSecond,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = System.currentTimeMillis(),
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun WorkoutExerciseEntity.toDto() = WorkoutExerciseDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    sessionId = sessionId,
    exerciseId = this.exerciseId,
    order = this.order,
    restSecond = this.restSecond
)

fun WorkoutExerciseDto.toEntity(sessionId: String): WorkoutExerciseEntity = WorkoutExerciseEntity(
    sessionId = sessionId,
    exerciseId = this.exerciseId,
    order = this.order,
    ID = this.id,
    restSecond = this.restSecond,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- DE SETS ---

fun ExerciseSetDto.toDomain() = ExerciseSet(
    id = this.id,
    setNumber = this.setNumber,
    reps = this.reps,
    weight = this.weight,
    rpe = this.rpe
)

fun ExerciseSetEntity.toDomain() = ExerciseSet(
    id = this.ID,
    setNumber = this.setNumber,
    reps = this.reps,
    weight = this.weight,
    rpe = this.rpe
)

fun ExerciseSet.toEntity(workoutExerciseId: String): ExerciseSetEntity = ExerciseSetEntity(
    workoutExerciseId = workoutExerciseId,
    setNumber = this.setNumber,
    reps = this.reps,
    weight = this.weight,
    rpe = this.rpe,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun ExerciseSetEntity.toDto() = ExerciseSetDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    setNumber = this.setNumber,
    workoutExerciseId = workoutExerciseId,
    reps = this.reps,
    weight = this.weight,
    rpe = this.rpe
)
fun ExerciseSetDto.toEntity(workoutExerciseId: String): ExerciseSetEntity = ExerciseSetEntity(
    workoutExerciseId = workoutExerciseId,
    setNumber = this.setNumber,
    reps = this.reps,
    weight = this.weight,
    rpe = this.rpe,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

