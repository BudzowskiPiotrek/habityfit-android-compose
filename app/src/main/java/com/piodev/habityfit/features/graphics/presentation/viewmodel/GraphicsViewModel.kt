package com.piodev.habityfit.features.graphics.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.graphics.domain.repository.GraphicsRepository
import com.piodev.habityfit.features.graphics.presentation.state.ExerciseSeriesUiItem
import com.piodev.habityfit.features.graphics.presentation.state.GraphicsLoadState
import com.piodev.habityfit.features.graphics.presentation.state.GraphicsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GraphicsViewModel @Inject constructor(
    private val repository: GraphicsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val routineId: String = savedStateHandle["routineId"] ?: ""

    var state by mutableStateOf(GraphicsState())
        private set

    init {
        loadSeries()
    }

    private fun loadSeries() {
        viewModelScope.launch {
            state = state.copy(loadState = GraphicsLoadState.Loading)
            try {
                repository.getProgressSeriesForRoutine(routineId)
                    .collect { seriesList ->
                        state = if (seriesList.isEmpty()) {
                            state.copy(loadState = GraphicsLoadState.Empty)
                        } else {
                            val items = seriesList
                                .filter { it.dataPoints.isNotEmpty() }
                                .map { ExerciseSeriesUiItem(series = it, isVisible = true) }

                            if (items.isEmpty()) {
                                state.copy(loadState = GraphicsLoadState.Empty)
                            } else {
                                state.copy(loadState = GraphicsLoadState.Success(items))
                            }
                        }
                    }
            } catch (e: Exception) {
                state = state.copy(
                    loadState = GraphicsLoadState.Error(
                        e.message ?: "Error desconocido"
                    )
                )
            }
        }
    }

    fun toggleExerciseVisibility(exerciseId: String) {
        val current = state.loadState
        if (current !is GraphicsLoadState.Success) return

        val updated = current.items.map { item ->
            if (item.series.exerciseId == exerciseId) {
                item.copy(isVisible = !item.isVisible)
            } else {
                item
            }
        }
        state = state.copy(loadState = current.copy(items = updated))
    }

    fun setAllVisible(visible: Boolean) {
        val current = state.loadState
        if (current !is GraphicsLoadState.Success) return

        val updated = current.items.map { it.copy(isVisible = visible) }
        state = state.copy(loadState = current.copy(items = updated))
    }
}