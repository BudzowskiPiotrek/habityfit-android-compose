package com.piodev.habityfit.features.community.domain.model

import com.piodev.database.model.RequestStatus

data class RoutineRequest(
    val id: String,
    val senderId: String,
    val senderEmail: String,
    val routineId: String,
    val routineName: String,
    val status: RequestStatus
)