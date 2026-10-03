package com.piodev.habityfit.features.workout.presentation.state

import com.piodev.habityfit.features.routine.domain.model.Routine

data class WorkoutFormState(
    val workoutId: String? = null,
    val date: Long = System.currentTimeMillis(),
    val durationMinutes: String = "0",
    val notes: String = "",
    val routines: List<Routine> = emptyList(),
    val selectedRoutineId: String? = null,
    val selectedRoutineName: String? = null,

    val searchQuery: String = "",

    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
    val isDataLoaded: Boolean? = null
)
