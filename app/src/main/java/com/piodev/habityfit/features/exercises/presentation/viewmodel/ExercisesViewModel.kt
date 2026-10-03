package com.piodev.habityfit.features.exercises.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.util.normalizeForSearch
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import com.piodev.habityfit.features.exercises.presentation.state.ExercisesState
import com.piodev.habityfit.features.graphics.domain.repository.GraphicsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val repository: ExerciseRepository,
    private val graphicsRepository: GraphicsRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESTADO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(ExercisesState())
        private set
    private val _selectedMuscleGroupId = MutableStateFlow<String?>(null)
    private val _selectedExerciseId = MutableStateFlow<String?>(null)
    private val _query = MutableStateFlow("")

    init {
        observeExercises()
        observeSeries()
    }

    fun onMuscleSelected(muscleId: String?) {
        val newId = if (muscleId == _selectedMuscleGroupId.value)
            null else muscleId
        _selectedMuscleGroupId.value = newId
        state = state.copy(selectedMuscleGroupId = newId)
    }

    fun onQueryChange(query: String) {
        _query.value = query
        state = state.copy(query = query)
    }

    fun onExerciseSelected(exerciseId: String?) {
        _selectedExerciseId.value = exerciseId
        val exercise = if (exerciseId == null)
            null else state.exercises.find { it.id == exerciseId }
        state = state.copy(selectedExercise = exercise)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE OBTENER EXERSISIOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeExercises() {
        viewModelScope.launch {
            combine(
                _selectedMuscleGroupId.flatMapLatest { muscleId ->
                    if (muscleId == null) repository.getExercises()
                    else repository.getExercisesByMuscleGroup(muscleId)
                },
                _query
            ) { list, query ->
                val queryTokens = query.split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.normalizeForSearch() }

                if (queryTokens.isEmpty()) {
                    list
                } else {
                    list.filter { exercise ->
                        queryTokens.all { token ->
                            exercise.name.normalizeForSearch().contains(token)
                        }
                    }
                }
            }.collect { filtered ->
                state = state.copy(exercises = filtered)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSeries() {
        viewModelScope.launch {
            _selectedExerciseId.flatMapLatest { id ->
                if (id == null) flowOf(null)
                else graphicsRepository.getProgressSeriesForExercise(id)
            }.collect { series ->
                state = state.copy(exerciseSeries = series)
            }
        }
    }
}