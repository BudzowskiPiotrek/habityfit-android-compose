package com.piodev.network.dto.sync

import com.piodev.database.model.FeedEventType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class FeedEventDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "USER_ID") val userId: String,
    @Json(name = "EVENT_TYPE") val eventType: FeedEventType,
    @Json(name = "EXERCISE_ID") val exerciseId: String? = null,
    @Json(name = "METRIC_VALUE") val metricValue: Double? = null,
    @Json(name = "CUSTOM_TEXT") val customText: String? = null,
)
