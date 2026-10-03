package com.piodev.habityfit.features.informe.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.informe.domain.model.InformePeriod
import com.piodev.habityfit.features.informe.domain.repository.InformeRepository
import com.piodev.habityfit.features.informe.presentation.state.InformeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformeViewModel @Inject constructor(
    private val repository: InformeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(InformeState())
        private set

    private val _period = MutableStateFlow(InformePeriod.MONTH)

    init {
        observeInforme()
    }

    fun onPeriodChange(newPeriod: InformePeriod) {
        if (newPeriod == _period.value) return
        state = state.copy(period = newPeriod, isLoading = true)
        _period.value = newPeriod
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeInforme() {
        viewModelScope.launch {
            authRepository.getCurrentUser().flatMapLatest { user ->
                val userId = user?.id
                if (userId == null) {
                    flowOf(null)
                } else {
                    _period.flatMapLatest { period ->
                        repository.getInforme(userId, period)
                    }
                }
            }.collect { summary ->
                state = state.copy(
                    summary = summary,
                    isLoading = false,
                    error = if (summary == null)
                        "No se pudo cargar el informe"
                    else
                        null
                )
            }
        }
    }
}