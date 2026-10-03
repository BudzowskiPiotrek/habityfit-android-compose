package com.piodev.habityfit.features.informe.domain.repository

import com.piodev.habityfit.features.informe.domain.model.InformePeriod
import com.piodev.habityfit.features.informe.domain.model.InformeSummary
import kotlinx.coroutines.flow.Flow

interface InformeRepository {
    fun getInforme(userId: String, period: InformePeriod): Flow<InformeSummary>
}