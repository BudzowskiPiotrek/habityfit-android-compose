package com.piodev.habityfit.features.rm.domain.usecase

import com.piodev.habityfit.core.config.LimitsConfig.BRZYCKI_CONST_1
import com.piodev.habityfit.core.config.LimitsConfig.BRZYCKI_CONST_2
import com.piodev.habityfit.core.config.LimitsConfig.MAX_ALLOWED_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_ALLOWED_WEIGHT
import com.piodev.habityfit.features.rm.domain.model.RMResult

class RMCalculateUseCase {
    operator fun invoke(weight: Double, reps: Int): List<RMResult> {
        if (reps <= 0 || weight <= 0 || reps > MAX_ALLOWED_REPS || weight > MAX_ALLOWED_WEIGHT) {
            return emptyList()
        }
        val oneRM = weight / (BRZYCKI_CONST_1 - (BRZYCKI_CONST_2 * reps))
        val repSubsets = listOf(1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 18, 20)
        return repSubsets.map { r ->
            val estimatedWeight = oneRM * (BRZYCKI_CONST_1 - (BRZYCKI_CONST_2 * r))
            RMResult(r, if (estimatedWeight > 0) estimatedWeight else 0.0)
        }
    }
}