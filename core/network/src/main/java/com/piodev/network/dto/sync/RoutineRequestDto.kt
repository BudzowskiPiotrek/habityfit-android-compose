package com.piodev.network.dto.sync

import com.piodev.database.model.RequestStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class RoutineRequestDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "SENDER_ID") val senderId: String,
    @Json(name = "RECEIVER_ID") val receiverId: String,
    @Json(name = "ROUTINE_ID") val routineId: String,
    @Json(name = "STATUS") val status: RequestStatus
)