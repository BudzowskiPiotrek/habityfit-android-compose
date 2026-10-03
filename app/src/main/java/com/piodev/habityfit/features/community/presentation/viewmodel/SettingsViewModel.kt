package com.piodev.habityfit.features.community.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.database.model.Sex
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.community.domain.model.User
import com.piodev.habityfit.features.community.domain.repository.SettingsRepository
import com.piodev.habityfit.features.community.presentation.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settings: SettingsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // STATE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()
    private var initialUser: User? = null

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            authRepository.getCurrentUser()
                .flatMapLatest { user ->
                    if (user != null) settings.getUser(user.id)
                    else flowOf(null)
                }
                .collect { user ->
                    user?.let {
                        if (initialUser == null) {
                            initialUser = it
                            updateStateFromUser(it)
                        }
                    }
                }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // EVENTOS DE UI (Para el formulario)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun onNameChange(newName: String) {
        _state.update { it.copy(fullName = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _state.update { it.copy(email = newEmail) }
    }

    fun onSexChange(newSex: Sex?) {
        _state.update { it.copy(sex = newSex) }
    }

    fun onBirthDateChange(newDate: Long?) {
        _state.update { it.copy(birthDate = newDate) }
    }

    fun onHeightChange(newHeight: Double?) {
        _state.update { it.copy(height = newHeight) }
    }

    fun dismissError() {
        _state.update { it.copy(errorMessage = null) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE ACTUALIZAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun updateStateFromUser(user: User) {
        _state.update { currentState ->
            currentState.copy(
                id = user.id,
                email = user.email,
                fullName = user.fullName,
                sex = user.sex,
                birthDate = user.birthDate,
                height = user.height,
            )
        }
    }

    fun saveProfile() {
        val currentState = _state.value
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val userDomain = User(
                id = currentState.id,
                email = currentState.email,
                fullName = currentState.fullName,
                sex = currentState.sex,
                birthDate = currentState.birthDate,
                height = currentState.height,
            )

            settings.updateProfile(userDomain)
                .onSuccess {
                    initialUser = userDomain
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE DESACTIVAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun deactivateAccount() {
        val userId = _state.value.id
        if (userId.isBlank()) return

        viewModelScope.launch {
            settings.deactivateAccount(userId)
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message) }
                }
        }
    }
}
