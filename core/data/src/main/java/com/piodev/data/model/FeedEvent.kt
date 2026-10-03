package com.piodev.data.model

import com.piodev.database.model.FeedEventType

data class FeedEvent(
    val id: String,
    val userId: String,
    val eventType: FeedEventType,
    val exerciseId: String? = null,
    val metricValue: Double? = null,
    val customText: String? = null
)
