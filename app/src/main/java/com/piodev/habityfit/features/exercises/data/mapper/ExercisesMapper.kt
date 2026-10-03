package com.piodev.habityfit.features.exercises.data.mapper

import com.piodev.database.entities.MuscleGroupEntity
import com.piodev.database.model.ExerciseWithMuscleGroups
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.exercises.domain.model.MuscleGroup

fun MuscleGroupEntity.toDomain(): MuscleGroup {
    return MuscleGroup(
        id = this.ID,
        name = this.name
    )
}

fun ExerciseWithMuscleGroups.toDomain(): Exercise {
    return Exercise(
        id = exercise.ID,
        name = exercise.name,
        equipment = exercise.equipmentType,
        imageUrl = exercise.imageUrl,
        description = exercise.description,
        muscleGroups = muscleGroups.map { it.toDomain() }
    )
}

