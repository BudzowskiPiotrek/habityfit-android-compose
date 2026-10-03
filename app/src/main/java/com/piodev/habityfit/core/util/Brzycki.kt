package com.piodev.habityfit.core.util

import com.piodev.habityfit.core.config.LimitsConfig.BRZYCKI_CONST_1
import com.piodev.habityfit.core.config.LimitsConfig.BRZYCKI_CONST_2

fun estimateOneRM(weight: Double, reps: Int): Double {
    val limitReps = if (reps >= 18) 18 else reps
    val effectiveWeight = if (weight <= 0.0) 1.0 else weight
    return effectiveWeight / (BRZYCKI_CONST_1 - (BRZYCKI_CONST_2 * limitReps))
}

fun calculateEffectiveWeight(weight: Double, order: Int, restSeconds: Int): Double {
    val positionFactor = when (order) {
        in 0..3 -> 0.95
        in 4..6 -> 1.00
        in 7..9 -> 1.07
        else -> 1.12 // 7+
    }

    val restFactor = when {
        restSeconds >= 120 -> 1.00
        restSeconds in 90..119 -> 1.05
        restSeconds in 1..89  -> 1.10
        else -> 1.00
    }

    return weight * positionFactor * restFactor
}
