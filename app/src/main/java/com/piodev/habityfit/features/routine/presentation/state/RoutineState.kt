package com.piodev.habityfit.features.routine.presentation.state

import com.piodev.habityfit.features.community.domain.model.UserRelations
import com.piodev.habityfit.features.routine.domain.model.Routine
import com.piodev.habityfit.features.routine.domain.model.RoutineRequest

data class RoutineState(
    val pendingRoutineId: String? = null,
    val pendingRequest: RoutineRequest? = null,
    val routines: List<Routine> = emptyList(),
    val friends: List<UserRelations> = emptyList()
)