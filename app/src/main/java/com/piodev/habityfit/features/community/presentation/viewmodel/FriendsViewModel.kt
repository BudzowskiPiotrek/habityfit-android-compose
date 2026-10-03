package com.piodev.habityfit.features.community.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.data.repository.SyncRepository
import com.piodev.habityfit.core.config.LimitsConfig.EMAIL_REGEX
import com.piodev.habityfit.core.config.LimitsConfig.GUID_REGEX
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.community.domain.repository.FriendsRepository
import com.piodev.habityfit.features.community.presentation.state.FriendsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repository: FriendsRepository,
    private val authRepository: AuthRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESTADO Y EVENTOS DE FORMULÁRIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(FriendsState())
        private set

    fun onFriendCodeChange(newValue: String, isEmailMode: Boolean = false) {
        val error = when {
            newValue.isBlank() -> null
            isEmailMode && !EMAIL_REGEX.matches(newValue) -> "Formato de email incorrecto"
            !isEmailMode && !GUID_REGEX.matches(newValue) -> "Formato de código incorrecto"
            else -> null
        }
        state = state.copy(friendCodeInput = newValue, error = null, friendCodeError = error)
    }

    init {
        loadSocialData()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun loadSocialData() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                val userId = user?.id ?: return@collect

                state = state.copy(currentUserId = userId, isLoading = true)

                combine(
                    repository.getFriends(userId),
                    repository.getPendingRequests(userId),
                    repository.getSentRequests(userId)
                ) { friends, received, sent ->
                    state.copy(
                        friends = friends,
                        pendingReceived = received,
                        pendingSent = sent,
                        isLoading = false
                    )
                }.collect { updatedState ->
                    state = updatedState
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ENVIO DE SOLICITUD DE AMISTAD
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun sendFriendRequest() {
        val friendCode = state.friendCodeInput.trim()
        val userId = state.currentUserId
        if (friendCode.isEmpty()) return
        if (friendCode == userId) {
            state = state.copy(error = "No puedes añadirte a ti mismo"); return
        }
        if (!GUID_REGEX.matches(friendCode)) {
            state = state.copy(friendCodeError = "Formato de código incorrecto"); return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.sendFriendRequest(friendCode, userId)
                .onSuccess {
                    syncRepository.pushLocalChanges()
                    state = state.copy(
                        friendCodeInput = "",
                        isLoading = false,
                        error = null,
                        friendCodeError = null
                    )
                }
                .onFailure { exception ->
                    state = state.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }

    fun removeFriend(relationId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.declineFriendRequest(relationId)
                .onSuccess {
                    state = state.copy(isLoading = false, error = null)
                }
                .onFailure { exception ->
                    state = state.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }

    fun sendInvitation() {
        val email = state.friendCodeInput.trim()
        val userId = state.currentUserId
        if (email.isEmpty()) return
        if (!EMAIL_REGEX.matches(email)) {
            state = state.copy(friendCodeError = "Formato de email incorrecto"); return
        }
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.sendInvitation(email, userId)
                .onSuccess {
                    syncRepository.pushLocalChanges()
                    state = state.copy(
                        friendCodeInput = "",
                        isLoading = false,
                        error = null,
                        friendCodeError = null
                    )
                }
                .onFailure { exception ->
                    state = state.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }
}
