package com.piodev.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.piodev.database.entities.ExerciseEntity
import com.piodev.database.entities.ExerciseSetEntity

import com.piodev.database.entities.WorkoutSessionEntity
import com.piodev.database.entities.WorkoutExerciseEntity


data class WorkoutSessionWithExercises(
    @Embedded val session: WorkoutSessionEntity,

    @Relation(
        entity = WorkoutExerciseEntity::class, // Especificar la entidad base
        parentColumn = "ID",                   // PK de WorkoutSessionEntity
        entityColumn = "SESSION_ID"            // FK en WorkoutExerciseEntity
    )
    val exerciseConfigs: List<WorkoutExerciseWithDetails>
)

data class WorkoutExerciseWithDetails(
    @Embedded val config: WorkoutExerciseEntity,

    @Relation(
        entity = ExerciseEntity::class,
        parentColumn = "EXERCISE_ID", // FK en WorkoutExerciseEntity
        entityColumn = "ID"            // PK en ExerciseEntity
    )
    val exercise: ExerciseEntity,

    @Relation(
        entity = ExerciseSetEntity::class,
        parentColumn = "ID",                  // PK en WorkoutExerciseEntity
        entityColumn = "WORKOUT_EXERCISE_ID"  // FK en ExerciseSetEntity
    )
    val sets: List<ExerciseSetEntity>
)