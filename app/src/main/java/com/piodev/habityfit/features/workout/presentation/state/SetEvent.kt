package com.piodev.habityfit.features.workout.presentation.state

sealed class SetEvent {
    data class OnWeightChanged(val id: String, val value: String) : SetEvent()
    data class OnRepsChanged(val id: String, val value: String) : SetEvent()
    data class OnRpeChanged(val id: String, val value: String) : SetEvent()
    data class OnStartTimer(val exerciseId: String) : SetEvent()
}