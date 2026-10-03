package com.piodev.habityfit.features.routine.presentation.state

import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.routine.domain.model.RoutineExercise

data class RoutineEditorState(
    val routineId: String? = null,
    val name: String = "",
    val description: String = "",
    val selectedExercises: List<RoutineExercise> = emptyList(),
    val selectedExerciseForConfig: Exercise? = null,

    val searchQuery: String = "",
    val searchResults: List<Exercise> = emptyList(),
    val configForm: ExerciseConfigForm = ExerciseConfigForm(),

    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
    val isDataLoaded: Boolean? = null
)

data class ExerciseConfigForm(
    val sets: String = "3",
    val reps: String = "10",
    val rest: String = "120",
    val weight: String = "0.0"
)