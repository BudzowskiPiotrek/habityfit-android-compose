package com.piodev.habityfit.features.routine.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig.MAX_EXERCISE_WEIGHT
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REST_SECONDS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SETS
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.core.util.normalizeInt
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.routine.presentation.state.ExerciseEditorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseEditViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT ESTADO Y EVENTOS DE FORMULARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(ExerciseEditorState())
        private set

    fun onSetsChange(newSets: String) {

        state = state.copy(sets = normalizeInt(input = newSets, max = MAX_SETS))
    }

    fun onRepsChange(newReps: String) {
        state = state.copy(reps = normalizeInt(input = newReps, max = MAX_REPS))
    }

    fun onRepsSecondChange(newRepsSecond: String) {
        state = state.copy(restSecond = normalizeInt(input = newRepsSecond, max = MAX_REST_SECONDS))
    }

    fun onTargetWeightChange(newTarget: String) {
        state = state.copy(targetWeight = normalizeDouble(newTarget, max = MAX_EXERCISE_WEIGHT))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ACTUALIZACION Y CARGA DE EJERCICIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun updateFromMap(values: Map<String, String>) {
        values["sets"]?.let { onSetsChange(it) }
        values["reps"]?.let { onRepsChange(it) }
        values["restSecond"]?.let { onRepsSecondChange(it) }
        values["targetWeight"]?.let { onTargetWeightChange(it) }
        updateExercise()
    }

    private fun updateExercise() {
        val currentId = state.exerciseId.ifBlank { return }

        state = state.copy(isLoading = true)
        viewModelScope.launch {
            try {
                routineRepository.updateExerciseConfiguration(
                    id = currentId,
                    sets = state.sets.toInt(),
                    reps = state.reps.toInt(),
                    repsSeconds = state.restSecond.toInt(),
                    weight = state.targetWeight.toDoubleOrNull()
                )
                state = state.copy(isSaved = true)
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun loadExercise(exerciseId: String?) {
        if (exerciseId.isNullOrBlank()) return
        state = state.copy(
            exerciseId = exerciseId,
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val exercise = routineRepository.getExerciseById(exerciseId)
                if (exercise != null) {
                    state = state.copy(
                        sets = exercise.sets.toString(),
                        reps = exercise.reps.toString(),
                        restSecond = exercise.restSecond.toString(),
                        targetWeight = exercise.targetWeight?.toString() ?: ""
                    )
                } else {
                    state = state.copy(error = "El ejercicio no existe en la base de datos")
                }
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}
