package com.piodev.habityfit.features.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.data.repository.SyncRepository
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.community.domain.repository.FriendsRepository
import com.piodev.habityfit.features.community.domain.repository.RoutineRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AccountMenuViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val routineRequestRepository: RoutineRequestRepository,
    private val friendsRepository: FriendsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val hasPendingRoutineRequests: StateFlow<Boolean> = authRepository.getCurrentUser()
        .flatMapLatest { user ->
            if (user != null) routineRequestRepository.getReceivedRequests(user.id)
            else flowOf(emptyList())
        }
        .map { it.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val hasPendingFriendRequests: StateFlow<Boolean> = authRepository.getCurrentUser()
        .flatMapLatest { user ->
            if (user != null) friendsRepository.getPendingRequests(user.id)
            else flowOf(emptyList())
        }
        .map { it.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun sync() {
        viewModelScope.launch {
            syncRepository.syncAll()
        }
    }
}