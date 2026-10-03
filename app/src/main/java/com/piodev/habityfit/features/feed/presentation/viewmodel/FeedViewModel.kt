package com.piodev.habityfit.features.feed.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import com.piodev.habityfit.features.feed.domain.repository.FeedRepository
import com.piodev.habityfit.features.feed.presentation.state.FeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: FeedRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    var state by mutableStateOf(FeedState())
        private set

    init {
        loadFeed()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun loadFeed() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            repository.getFeedEvents().collect { events ->
                state = state.copy(
                    events = events,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}
