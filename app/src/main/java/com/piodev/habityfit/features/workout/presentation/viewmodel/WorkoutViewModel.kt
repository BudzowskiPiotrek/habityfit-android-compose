package com.piodev.habityfit.features.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.core.config.LimitsConfig.MAX_WORKOUT_DURATION
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.workout.domain.model.WorkoutSession
import com.piodev.habityfit.features.workout.domain.repository.WorkoutRepository
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
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // STATE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @OptIn(ExperimentalCoroutinesApi::class)
    val session: StateFlow<List<WorkoutSession>> = authRepository.getCurrentUser()
        .flatMapLatest { user ->
            if (user != null) repository.getSessions(user.id)
            else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun deleteWorkout(workoutId: String) {
        viewModelScope.launch {
            repository.deleteWorkoutSession(workoutId)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ACTUALIZAR TIEMPO DE FINALIZACION DE LA SESION
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun updateWorkoutDuration(workoutId: String) {

        viewModelScope.launch {
            var session = repository.getWorkoutById(workoutId)
            if (session != null) {
                val currentTime = System.currentTimeMillis()
                var duration = ((currentTime - session.date) / 60000).toInt()
                if (duration > MAX_WORKOUT_DURATION) duration = MAX_WORKOUT_DURATION
                repository.updateWorkoutDuration(sessionId = workoutId, duration = duration)
            }
        }
    }
}