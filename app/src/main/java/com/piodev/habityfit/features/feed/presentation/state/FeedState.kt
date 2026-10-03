package com.piodev.habityfit.features.feed.presentation.state

import com.piodev.habityfit.features.feed.domain.model.FeedEvent

data class FeedState(
    val events: List<FeedEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)