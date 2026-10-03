package com.piodev.habityfit.features.community.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.community.domain.repository.FriendsRepository
import com.piodev.habityfit.features.community.presentation.state.CommunityState
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: FriendsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(CommunityState())
        private set

    init {
        loadRequests()
    }

    private fun loadRequests() {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                val userId = user?.id ?: return@collect
                state = state.copy(isLoading = true)
                combine(
                    repository.getSentRequests(userId),
                    repository.getPendingRequests(userId)
                ) { sent, received ->
                    state.copy(
                        sentRequests = sent,
                        receivedRequests = received,
                        isLoading = false
                    )
                }.collect { updatedState ->
                    state = updatedState
                }
            }
        }
    }

    fun acceptRequest(relationId: String) {
        viewModelScope.launch {
            repository.acceptFriendRequest(relationId)
        }
    }

    fun declineRequest(relationId: String) {
        viewModelScope.launch {
            repository.declineFriendRequest(relationId)
        }
    }
}
