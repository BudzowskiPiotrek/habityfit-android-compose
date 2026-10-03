package com.piodev.habityfit.features.exercises.presentation.state

import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.graphics.domain.model.ExerciseProgressSeries

data class ExercisesState(
    val selectedMuscleGroupId: String? = null,
    val exercises: List<Exercise> = emptyList(),
    val exerciseSeries: ExerciseProgressSeries? = null,
    val selectedExercise: Exercise? = null,
    val query: String = ""
)