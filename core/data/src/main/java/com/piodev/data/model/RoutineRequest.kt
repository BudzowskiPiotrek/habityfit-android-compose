package com.piodev.data.model

import com.piodev.database.model.RequestStatus

data class RoutineRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val routineId: String,
    val status: RequestStatus,
)