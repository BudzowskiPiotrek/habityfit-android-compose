package com.piodev.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.piodev.database.entities.ExerciseEntity
import com.piodev.database.entities.RoutineEntity
import com.piodev.database.entities.RoutineExerciseEntity

data class RoutineWithExercises(
    @Embedded val routine: RoutineEntity,

    @Relation(
        entity = RoutineExerciseEntity::class,
        parentColumn = "ID",
        entityColumn = "ROUTINE_ID"
    )
    val exerciseConfigs: List<RoutineExerciseWithDetails>
)

data class RoutineExerciseWithDetails(
    @Embedded val config: RoutineExerciseEntity,

    @Relation(
        parentColumn = "EXERCISE_ID",
        entityColumn = "ID"
    )
    val exercise: ExerciseEntity
)