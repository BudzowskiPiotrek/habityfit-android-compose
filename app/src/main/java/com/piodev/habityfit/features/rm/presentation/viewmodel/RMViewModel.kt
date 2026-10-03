package com.piodev.habityfit.features.rm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.piodev.habityfit.core.config.LimitsConfig.MAX_RM_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_RM_WEIGHT
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.core.util.normalizeInt
import com.piodev.habityfit.features.rm.domain.usecase.RMCalculateUseCase
import com.piodev.habityfit.features.rm.presentation.state.RMState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RMViewModel(
    private val calculateMaxEstimatesUseCase: RMCalculateUseCase = RMCalculateUseCase()
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT ESTADO Y EVENTOS DEL 1RM
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val _state = MutableStateFlow(RMState())
    val state: StateFlow<RMState> = _state.asStateFlow()

    init {
        updateResults()
    }

    fun onWeightChange(newWeight: Double) {
        if (newWeight >= 0) {
            val normalizer = normalizeDouble(newWeight.toString(), max = MAX_RM_WEIGHT).toDouble()
            _state.update { it.copy(weight = normalizer) }
            updateResults()
        }
    }

    fun onRepsChange(newReps: Int) {
        if (newReps > 0) {
            val normalizer = normalizeInt(newReps.toString(), max = MAX_RM_REPS).toInt()
            _state.update { it.copy(reps = normalizer) }
            updateResults()
        }
    }

    fun onWeightManualChange(input: String) {
        if (input.isEmpty()) {
            _state.update { it.copy(weight = 0.0) }
            return
        }
        input.toDoubleOrNull()?.let { value ->
            val normalizer = normalizeDouble(value.toString(), max = MAX_RM_WEIGHT).toDouble()
            _state.update { it.copy(weight = normalizer) }
            updateResults()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CAMBIO DE RESULTADO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun updateResults() {
        val current = _state.value
        val newResults = calculateMaxEstimatesUseCase(current.weight, current.reps)
        _state.update { it.copy(results = newResults) }
    }
}