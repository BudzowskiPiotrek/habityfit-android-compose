package com.piodev.habityfit.features.workout.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig.MAX_WORKOUT_DURATION
import com.piodev.habityfit.core.config.LimitsConfig.MAX_WORKOUT_NOTES
import com.piodev.habityfit.core.util.limitTextLength
import com.piodev.habityfit.core.util.normalizeInt
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.workout.domain.model.WorkoutSession
import com.piodev.habityfit.features.workout.domain.repository.WorkoutRepository
import com.piodev.habityfit.features.workout.presentation.state.WorkoutFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutFormViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val routineRepository: RoutineRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT ESTADO Y EVENTOS DE FORMULARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(WorkoutFormState())
        private set

    init {
        searchRoutines("")
    }

    fun onDurationChange(newDuration: String) {
        if (newDuration.all { it.isDigit() }) {
            state = state.copy(
                durationMinutes = normalizeInt(newDuration, max = MAX_WORKOUT_DURATION)
            )
        }
    }

    fun onRoutineSelected(routineId: String, routineName: String) {
        state = state.copy(
            selectedRoutineId = routineId,
            selectedRoutineName = routineName,
            searchQuery = ""
        )
    }

    fun onNotesChange(newNotes: String) {
        state = state.copy(notes = limitTextLength(newNotes, maxLength = MAX_WORKOUT_NOTES))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE BUSCA DE RUTINAS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun onSearchQueryChange(newQuery: String) {
        state = state.copy(searchQuery = newQuery)
        searchRoutines(newQuery)
    }

    private fun searchRoutines(query: String) {
        viewModelScope.launch {
            state = state.copy(isSearching = true)
            val userId = authRepository.getCurrentUser().first()?.id ?: ""
            if (userId.isNotBlank()) {
                routineRepository.getRoutines(userId).collect { routines ->
                    val filtered = if (query.isBlank()) {
                        routines
                    } else {
                        routines.filter { it.name.contains(query, ignoreCase = true) }
                    }
                    state = state.copy(routines = filtered, isSearching = false)
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE GUARDADO, ELIMINACIÓN Y REORDENAMIENTO DE RUTINAS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun saveWorkout() {
        val routineId = state.selectedRoutineId ?: return
        val durationInt = state.durationMinutes.toIntOrNull() ?: 0
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val currentId = state.workoutId ?: ""
                val session = WorkoutSession(
                    id = currentId,
                    date = state.date,
                    durationMinutes = durationInt,
                    notes = state.notes.ifBlank { null }
                )
                val userId = authRepository.getCurrentUser().first()?.id ?: ""
                if (state.workoutId.isNullOrBlank()) {
                    workoutRepository.saveWorkoutSession(
                        userId = userId,
                        routineId = routineId,
                        session = session
                    )
                } else {
                    workoutRepository.updateWorkoutDetails(
                        currentId,
                        state.date,
                        durationInt,
                        state.notes.ifBlank { null }
                    )
                }
                state = state.copy(isSaved = true)
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun updateFromMap(values: Map<String, String>) {
        values["duration"]?.let { onDurationChange(it) }
        values["notes"]?.let { onNotesChange(it) }
        saveWorkout()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun loadWorkout(workoutId: String?) {
        if (workoutId.isNullOrBlank()) return
        state = state.copy(
            workoutId = workoutId,
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val workout = workoutRepository.getWorkoutById(workoutId)
                if (workout != null) {
                    state = state.copy(
                        date = workout.date,
                        durationMinutes = workout.durationMinutes.toString(),
                        notes = workout.notes ?: "",
                        selectedRoutineId = "ID_RECUPERADO",
                        isDataLoaded = true
                    )
                } else {
                    state = state.copy(error = "La sesion no existe en la base de datos")
                }
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}