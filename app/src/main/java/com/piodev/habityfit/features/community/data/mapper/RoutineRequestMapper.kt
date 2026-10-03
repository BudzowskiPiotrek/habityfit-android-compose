package com.piodev.habityfit.features.community.data.mapper

import com.piodev.database.model.RoutineRequestWithDetails
import com.piodev.habityfit.features.community.domain.model.RoutineRequest

fun RoutineRequestWithDetails.toDomain(): RoutineRequest {
    return RoutineRequest(
        id = this.request.ID,
        senderId = this.sender?.ID ?: "",
        senderEmail = this.sender?.email ?: "",
        routineId = this.request.routineId,
        routineName = this.routine?.name ?: "",
        status = this.request.status
    )
}