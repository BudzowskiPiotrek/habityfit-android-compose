package com.piodev.habityfit.features.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.database.entities.ExerciseEntity
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SET_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SET_RPE
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SET_WEIGHT
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.core.util.normalizeInt
import com.piodev.habityfit.features.workout.domain.model.WorkoutSet
import com.piodev.habityfit.features.workout.domain.repository.WorkoutRepository
import com.piodev.habityfit.features.workout.presentation.state.SetEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetEditViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT ESTADO Y EVENTOS DE FORMULARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private var currentWorkoutExerciseId: String? = null
    private var restSecondsForExercise: Int = 0
    private val _setsState = MutableStateFlow<List<WorkoutSet>>(emptyList())
    val setsState = _setsState.asStateFlow()
    private val _timerValue = MutableStateFlow(0)
    val timerValue = _timerValue.asStateFlow()

    private var timerJob: Job? = null
    private val updateJobs = mutableMapOf<String, Job>()

    private val _exercise = MutableStateFlow<ExerciseEntity?>(null)

    val exercise = _exercise.asStateFlow()

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // EVENTOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun onEvent(event: SetEvent) {
        when (event) {
            is SetEvent.OnWeightChanged -> handleWeight(event)
            is SetEvent.OnRepsChanged -> handleReps(event)
            is SetEvent.OnRpeChanged -> handleRpe(event)
            is SetEvent.OnStartTimer -> startTimer()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDACIONES
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun handleWeight(event: SetEvent.OnWeightChanged) {
        val value = event.value
        if (value.isEmpty()) {
            updateSet(event.id) { it.copy(weight = null) }
            return
        }
        if (!value.matches(Regex("""^\d*\.?\d*$"""))) return
        val parsed = value.toDoubleOrNull() ?: return
        var normalizer = normalizeDouble(parsed.toString(), max = MAX_SET_WEIGHT).toDouble()
        updateSet(event.id) { it.copy(weight = normalizer) }
    }

    private fun handleReps(event: SetEvent.OnRepsChanged) {
        val value = event.value
        if (!value.all { it.isDigit() }) return
        val parsed = value.toIntOrNull() ?: return
        var normalizer = normalizeInt(parsed.toString(), max = MAX_SET_REPS).toInt()
        updateSet(event.id) { it.copy(reps = normalizer) }
    }

    private fun handleRpe(event: SetEvent.OnRpeChanged) {
        val value = event.value
        if (value.isEmpty()) {
            updateSet(event.id) { it.copy(rpe = null) }
            return
        }
        if (!value.matches(Regex("""^\d*\.?\d*$"""))) return
        val parsed = value.toDoubleOrNull() ?: return
        var normalizer = normalizeDouble(parsed.toString(), max = MAX_SET_RPE).toDouble()
        updateSet(event.id) { it.copy(rpe = normalizer) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // TIMER
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun startTimer() {
        if (restSecondsForExercise <= 0) return
        _timerValue.value = restSecondsForExercise
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerValue.value > 0) {
                delay(1000)
                _timerValue.value -= 1
            }
        }
    }

    fun cancelTimer() {
        timerJob?.cancel()
        _timerValue.value = 0
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UPDATE LOCAL + DEBOUNCE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun updateSet(
        setId: String,
        update: (WorkoutSet) -> WorkoutSet
    ) {
        val current = _setsState.value.toMutableList()
        val index = current.indexOfFirst { it.id == setId }
        if (index == -1) return
        val updated = update(current[index])
        current[index] = updated
        _setsState.value = current
        debounceUpdate(updated)
    }

    private fun debounceUpdate(set: WorkoutSet) {
        val exerciseId = currentWorkoutExerciseId ?: return
        updateJobs[set.id]?.cancel()
        updateJobs[set.id] = viewModelScope.launch {
            delay(300)
            repository.updateSet(exerciseId, set)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOAD
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun loadSets(workoutExerciseId: String) {
        currentWorkoutExerciseId = workoutExerciseId
        viewModelScope.launch {
            restSecondsForExercise = repository.getRestSecondsForExercise(workoutExerciseId)
            _exercise.value = repository.getExerciseByWorkoutExerciseId(workoutExerciseId)
            repository.getSetsForExercise(workoutExerciseId)
                .collect { sets ->
                    _setsState.value = sets
                }
        }
    }
}