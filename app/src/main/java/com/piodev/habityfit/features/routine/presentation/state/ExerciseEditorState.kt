package com.piodev.habityfit.features.routine.presentation.state

data class ExerciseEditorState(
    val exerciseId: String = "",
    val sets: String = "",
    val reps: String = "",
    val order: String = "",
    val restSecond: String = "",
    val targetWeight: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
)
