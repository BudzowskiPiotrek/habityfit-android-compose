package com.piodev.habityfit.features.routine.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig.MAX_EXERCISE_WEIGHT
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REST_SECONDS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SETS
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.core.util.normalizeForSearch
import com.piodev.habityfit.core.util.normalizeInt
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import com.piodev.habityfit.features.routine.domain.model.Routine
import com.piodev.habityfit.features.routine.domain.model.RoutineExercise
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.routine.presentation.state.ExerciseConfigForm
import com.piodev.habityfit.features.routine.presentation.state.RoutineEditorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT ESTADO Y EVENTOS DE FORMULARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(RoutineEditorState())
        private set
    private val routineId: String = savedStateHandle["routineId"] ?: ""

    init {
        searchExercises("")
    }

    fun onExerciseSelectedForConfig(exercise: Exercise) {
        state = state.copy(selectedExerciseForConfig = exercise)
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        searchExercises(query)
    }

    fun onConfigFormChange(newForm: ExerciseConfigForm) {
        val normalizedForm = newForm.copy(
            sets = normalizeInt(input = newForm.sets, max = MAX_SETS),
            reps = normalizeInt(input = newForm.reps, max = MAX_REPS),
            rest = normalizeInt(input = newForm.rest, max = MAX_REST_SECONDS),
            weight = normalizeDouble(input = newForm.weight, max = MAX_EXERCISE_WEIGHT)
        )
        state = state.copy(configForm = normalizedForm)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    val routineState: StateFlow<Routine?> = routineRepository
        .getRoutineWithExercises(routineId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE BUSCA DE EXERCÍCIOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun searchExercises(query: String) {
        viewModelScope.launch {
            val queryTokens = query.split(" ")
                .filter { it.isNotEmpty() }
                .map { it.normalizeForSearch() }

            state = state.copy(isSearching = true)

            exerciseRepository.getExercises().collect { list ->
                val filtered = list.filter { exercise ->
                    queryTokens.all { token ->
                        exercise.name.normalizeForSearch().contains(token) ||
                                exercise.muscleGroups.any { muscle ->
                                    muscle.name.normalizeForSearch().contains(token)
                                }
                    }
                }
                state = state.copy(
                    searchResults = filtered,
                    isSearching = false
                )
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CONFIGURACION DE EJERCICIOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun addNewExercise(sets: Int, reps: Int, rest: Int, target: Double) {
        val exercise = state.selectedExerciseForConfig ?: return
        val currentExercises = routineState.value?.exercises ?: emptyList()
        val nextOrder = currentExercises.size

        viewModelScope.launch {
            val newExercise = RoutineExercise(
                id = "",
                exerciseId = exercise.id,
                name = exercise.name,
                sets = sets,
                reps = reps,
                order = nextOrder,
                restSecond = rest,
                targetWeight = target
            )
            routineRepository.saveRoutineExercise(newExercise, routineId)
        }
        cancelConfig()
    }

    fun cancelConfig() {
        state = state.copy(
            selectedExerciseForConfig = null,
            searchQuery = "",
            configForm = ExerciseConfigForm()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ELIMINACION DE EJERCICIOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun deleteExercise(exerciseId: String) {
        viewModelScope.launch {
            routineRepository.deleteExerciseFromRoutine(exerciseId)
        }
    }
}