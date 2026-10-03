package com.piodev.habityfit.core.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavOption(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val enabled: Boolean
)
