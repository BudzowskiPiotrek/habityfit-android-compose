package com.piodev.habityfit.features.community.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.community.domain.repository.RoutineRequestRepository
import com.piodev.habityfit.features.community.presentation.state.RoutineRequestState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineRequestViewModel @Inject constructor(
    private val repository: RoutineRequestRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RoutineRequestState())
        private set

    init {
        loadRequests()
    }

    private fun loadRequests() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                val userId = user?.id ?: return@collect
                state = state.copy(isLoading = true)
                repository.getReceivedRequests(userId).collect { requests ->
                    state = state.copy(
                        requests = requests,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    fun acceptRequest(requestId: String) {
        viewModelScope.launch {
            repository.acceptRequest(requestId)
                .onFailure { state = state.copy(error = it.message) }
        }
    }

    fun declineRequest(requestId: String) {
        viewModelScope.launch {
            repository.declineRequest(requestId)
                .onFailure { state = state.copy(error = it.message) }
        }
    }
}
