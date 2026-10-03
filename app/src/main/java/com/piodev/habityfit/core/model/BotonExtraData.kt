package com.piodev.habityfit.core.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BotonExtraData(
    val icon: ImageVector,
    val onClick: () -> Unit
)