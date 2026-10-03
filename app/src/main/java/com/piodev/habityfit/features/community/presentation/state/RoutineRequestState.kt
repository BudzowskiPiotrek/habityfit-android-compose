package com.piodev.habityfit.features.community.presentation.state

import com.piodev.habityfit.features.community.domain.model.RoutineRequest

data class RoutineRequestState(
    val requests: List<RoutineRequest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)