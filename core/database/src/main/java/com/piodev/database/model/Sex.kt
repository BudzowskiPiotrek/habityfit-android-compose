package com.piodev.database.model

enum class Sex(val value: Int) {
    MEN(1),
    WOMEN(2);

    companion object {
        fun fromInt(value: Int) = values().find { it.value == value }
    }
}