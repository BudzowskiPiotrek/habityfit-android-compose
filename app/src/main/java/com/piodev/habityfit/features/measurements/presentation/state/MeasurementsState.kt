package com.piodev.habityfit.features.measurements.presentation.state

data class MeasurementsState(
    val id: String? = null,
    val date: Long = System.currentTimeMillis(),
    val weight: String = "",
    val muscleMass: String = "",
    val fatMass: String = "",
    val bodyWater: String = "",
    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)