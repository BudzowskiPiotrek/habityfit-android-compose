package com.piodev.habityfit.features.measurements.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.measurements.domain.model.Body
import com.piodev.habityfit.features.measurements.domain.repository.MeasurementsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementsViewModel @Inject constructor(
    private val repository: MeasurementsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // STATE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @OptIn(ExperimentalCoroutinesApi::class)
    val measurements: StateFlow<List<Body>> = authRepository.getCurrentUser()
        .flatMapLatest { user ->
            if (user != null) repository.getBodyData(user.id)
            else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun deleteBody(bodyId: String) {
        viewModelScope.launch {
            repository.deleteBodyData(bodyId)
        }
    }
}