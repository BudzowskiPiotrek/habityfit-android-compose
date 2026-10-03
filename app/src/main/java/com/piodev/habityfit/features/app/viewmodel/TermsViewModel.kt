package com.piodev.habityfit.features.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun acceptTerms(onDone: () -> Unit) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUser().first()?.id ?: return@launch
            authRepository.acceptTerms(userId)
            onDone()
        }
    }
}
