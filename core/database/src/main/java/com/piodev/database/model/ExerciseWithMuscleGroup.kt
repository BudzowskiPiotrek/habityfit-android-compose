package com.piodev.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.piodev.database.entities.ExerciseEntity
import androidx.room.Junction
import com.piodev.database.entities.MuscleGroupEntity
import com.piodev.database.entities.ExerciseMuscleGroupEntity

data class ExerciseWithMuscleGroups(

    @Embedded
    val exercise: ExerciseEntity,

    @Relation(
        parentColumn = "ID",
        entityColumn = "ID",
        associateBy = Junction(
            value = ExerciseMuscleGroupEntity::class,
            parentColumn = "EXERCISE_ID",
            entityColumn = "MUSCLE_GROUP_ID"
        )
    )
    val muscleGroups: List<MuscleGroupEntity>
)