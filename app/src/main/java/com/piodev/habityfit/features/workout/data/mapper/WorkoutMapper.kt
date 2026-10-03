package com.piodev.habityfit.features.workout.data.mapper

import com.piodev.database.entities.ExerciseSetEntity
import com.piodev.database.entities.WorkoutExerciseEntity
import com.piodev.database.entities.WorkoutSessionEntity
import com.piodev.database.model.SyncStatus
import com.piodev.database.model.WorkoutSessionWithExercises
import com.piodev.habityfit.features.workout.domain.model.WorkoutSession
import com.piodev.database.model.WorkoutExerciseWithDetails
import com.piodev.habityfit.features.workout.domain.model.WorkoutExercise
import com.piodev.habityfit.features.workout.domain.model.WorkoutSet
import java.util.UUID

fun WorkoutSessionWithExercises.toDomain(): WorkoutSession {
    return WorkoutSession(
        id = this.session.ID,
        date = this.session.date,
        durationMinutes = this.session.durationMinutes,
        notes = this.session.notes,
        exercises = this.exerciseConfigs
            .sortedBy { it.config.order }
            .map { it.toDomain() }
    )
}

fun WorkoutExerciseWithDetails.toDomain(): WorkoutExercise {
    return WorkoutExercise(
        id = this.config.ID,
        name = this.exercise.name,
        exerciseId = this.exercise.ID,
        order = this.config.order,
        rest_seconds = this.config.restSecond,
        sets = this.sets
            .sortedBy { it.setNumber }
            .map { it.toDomain() },
        imageUrl = this.exercise.imageUrl
    )
}

fun WorkoutSessionEntity.toDomain(): WorkoutSession {
    return WorkoutSession(
        id = this.ID,
        date = this.date,
        durationMinutes = this.durationMinutes,
        notes = this.notes,
        exercises = emptyList()
    )
}

fun ExerciseSetEntity.toDomain(): WorkoutSet {
    return WorkoutSet(
        id = this.ID,
        setNumber = this.setNumber,
        reps = this.reps,
        weight = this.weight,
        rpe = this.rpe
    )
}

fun WorkoutSession.toEntity(userId: String): WorkoutSessionEntity {
    return WorkoutSessionEntity(
        ID = this.id.ifBlank { UUID.randomUUID().toString() },
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        userId = userId,
        date = this.date,
        durationMinutes = this.durationMinutes,
        notes = this.notes
    )
}

fun WorkoutExercise.toEntity(sessionId: String, exerciseId: String): WorkoutExerciseEntity {
    return WorkoutExerciseEntity(
        ID = this.id.ifBlank { UUID.randomUUID().toString() },
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        sessionId = sessionId,
        exerciseId = exerciseId,
        order = this.order,
        restSecond = this.rest_seconds
    )
}

fun WorkoutSet.toEntity(workoutExerciseId: String): ExerciseSetEntity {
    return ExerciseSetEntity(
        ID = this.id.ifBlank { UUID.randomUUID().toString() },
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        workoutExerciseId = workoutExerciseId,
        setNumber = this.setNumber,
        reps = this.reps,
        weight = this.weight,
        rpe = this.rpe
    )
}
