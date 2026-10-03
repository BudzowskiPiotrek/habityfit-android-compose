package com.piodev.habityfit.features.rm.presentation.state

import com.piodev.habityfit.features.rm.domain.model.RMResult

data class RMState(
    val weight: Double = 80.0,
    val reps: Int = 10,
    val results: List<RMResult> = emptyList()
)