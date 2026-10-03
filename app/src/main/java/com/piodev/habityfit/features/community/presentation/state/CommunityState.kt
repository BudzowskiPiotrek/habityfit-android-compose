package com.piodev.habityfit.features.community.presentation.state

import com.piodev.habityfit.features.community.domain.model.UserRelations

data class CommunityState(
    val sentRequests: List<UserRelations> = emptyList(),
    val receivedRequests: List<UserRelations> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
