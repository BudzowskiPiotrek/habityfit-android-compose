package com.piodev.habityfit.features.routine.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.data.repository.SyncRepository
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.community.domain.model.UserRelations
import com.piodev.habityfit.features.community.domain.repository.FriendsRepository
import com.piodev.habityfit.features.routine.domain.model.RoutineRequest
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.routine.presentation.state.RoutineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val authRepository: AuthRepository,
    private val friendsRepository: FriendsRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // STATE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val _state = MutableStateFlow(RoutineState())
    val state: StateFlow<RoutineState> = _state.asStateFlow()

    init {
        loadRoutines()
        loadFriends()
    }

    fun onPendingRoutineId(routineId: String) {
        _state.update { it.copy(pendingRoutineId = routineId, pendingRequest = null) }
    }

    fun onPendingRequest(relation: UserRelations) {
        val routineId = _state.value.pendingRoutineId ?: return
        _state.update {
            it.copy(
                pendingRequest = RoutineRequest(
                    targetUserId = relation.friend.id,
                    routineId = routineId
                )
            )
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun loadFriends() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                val userId = user?.id ?: return@collectLatest
                friendsRepository.getFriends(userId).collect { friends ->
                    _state.update { it.copy(friends = friends) }
                }
            }
        }
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collectLatest { user ->
                if (user != null) {
                    routineRepository.getRoutines(user.id).collect { routines ->
                        _state.update { it.copy(routines = routines) }
                    }
                } else {
                    _state.update { it.copy(routines = emptyList()) }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            routineRepository.deleteRoutine(routineId)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ENVIAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun onConfirmSend() {
        val request = _state.value.pendingRequest ?: return
        viewModelScope.launch {
            val senderId = authRepository.getCurrentUser().first()?.id ?: run {
                dismissAll()
                return@launch
            }
            routineRepository.sendRoutineRequest(request, senderId)
            syncRepository.pushLocalChanges()
        }
        dismissAll()
    }

    fun dismissAll() {
        _state.update { it.copy(pendingRoutineId = null, pendingRequest = null) }
    }
}