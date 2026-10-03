package com.piodev.habityfit.features.measurements.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.measurements.domain.model.Body
import com.piodev.habityfit.features.measurements.domain.repository.MeasurementsRepository
import com.piodev.habityfit.features.measurements.presentation.state.MeasurementsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementsFormViewModel @Inject constructor(
    private val repository: MeasurementsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESTADO Y EVENTOS DE FORMULÁRIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(MeasurementsState())
        private set

    fun onWeightChange(v: String) {
        state = state.copy(weight = normalizeDouble(v, max = LimitsConfig.MAX_HUMAN_WEIGHT))
    }

    fun onMuscleChange(v: String) {
        state = state.copy(muscleMass = normalizeDouble(v, max = LimitsConfig.MAX_HUMAN_MUSCLE))
    }

    fun onFatChange(v: String) {
        state = state.copy(fatMass = normalizeDouble(v, max = LimitsConfig.MAX_HUMAN_FAT))
    }

    fun onWaterChange(v: String) {
        state = state.copy(bodyWater = normalizeDouble(v, max = LimitsConfig.MAX_HUMAN_WATER))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE GUARDADO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun saveBodyData() {
        val w = state.weight.toDoubleOrNull() ?: return
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val userId = authRepository.getCurrentUser().first()?.id ?: ""
            if (userId.isNotBlank()) {
                val measurement = Body(
                    id = state.id ?: "",
                    date = state.date,
                    weight = w,
                    muscleMass = state.muscleMass.toDoubleOrNull(),
                    fatMass = state.fatMass.toDoubleOrNull(),
                    bodyWater = state.bodyWater.toDoubleOrNull()
                )
                if (state.id.isNullOrBlank()) {
                    repository.saveBodyData(userId, measurement)
                } else {
                    repository.updateBody(measurement)
                }
                state = state.copy(isSaved = true)
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun validateFormat(input: String, max: Double): Boolean {
        if (input.isEmpty()) return true
        val isValid = input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' }
        val value = input.toDoubleOrNull() ?: 0.0
        return isValid && value <= max
    }

    fun updateFromMap(values: Map<String, String>) {
        values["weight"]?.let { onWeightChange(it) }
        values["muscle"]?.let { onMuscleChange(it) }
        values["fat"]?.let { onFatChange(it) }
        values["water"]?.let { onWaterChange(it) }
        saveBodyData()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun loadBodyData(bodyId: String?) {
        if (bodyId == null) return
        state = state.copy(
            id = bodyId,
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val body = repository.getBodyById(bodyId)
                if (body != null) {
                    state = state.copy(
                        date = body.date,
                        weight = body.weight.toString(),
                        muscleMass = body.muscleMass?.toString() ?: "",
                        fatMass = body.fatMass?.toString() ?: "",
                        bodyWater = body.bodyWater?.toString() ?: "",
                        isDataLoaded = true
                    )
                } else {
                    state = state.copy(error = "La medición no existe en la base de datos")
                }
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}