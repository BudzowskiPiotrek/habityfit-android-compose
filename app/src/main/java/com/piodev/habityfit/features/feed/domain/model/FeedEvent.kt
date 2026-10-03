package com.piodev.habityfit.features.feed.domain.model

import com.piodev.database.model.FeedEventType

data class FeedEvent(
    val id: String = "",
    val userId: String,
    val eventType: FeedEventType,
    val exerciseId: String? = null,
    val metricValue: Double? = null,
    val customText: String? = null,
    val createdAt: Long = 0L
)
