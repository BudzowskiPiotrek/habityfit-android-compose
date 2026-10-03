package com.piodev.habityfit.features.community.presentation.state

import com.piodev.habityfit.features.community.domain.model.UserRelations

data class FriendsState(
    val friends: List<UserRelations> = emptyList(),
    val pendingReceived: List<UserRelations> = emptyList(),
    val pendingSent: List<UserRelations> = emptyList(),
    val friendCodeInput: String = "",
    val friendCodeError: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,

    val currentUserId: String = ""
)
