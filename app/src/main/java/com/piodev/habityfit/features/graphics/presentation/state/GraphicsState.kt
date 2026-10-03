package com.piodev.habityfit.features.graphics.presentation.state

import com.piodev.habityfit.features.graphics.domain.model.ExerciseProgressSeries

data class ExerciseSeriesUiItem(
    val series: ExerciseProgressSeries,
    val isVisible: Boolean = true
)

sealed class GraphicsLoadState {
    object Loading : GraphicsLoadState()
    object Empty : GraphicsLoadState()
    data class Success(val items: List<ExerciseSeriesUiItem>) : GraphicsLoadState()
    data class Error(val message: String) : GraphicsLoadState()
}

data class GraphicsState(
    val loadState: GraphicsLoadState = GraphicsLoadState.Loading,
    val routineName: String = ""
)
