package com.piodev.habityfit.features.informe.presentation.state

import com.piodev.habityfit.features.informe.domain.model.InformePeriod
import com.piodev.habityfit.features.informe.domain.model.InformeSummary

data class InformeState(
    val period: InformePeriod = InformePeriod.MONTH,
    val summary: InformeSummary? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)