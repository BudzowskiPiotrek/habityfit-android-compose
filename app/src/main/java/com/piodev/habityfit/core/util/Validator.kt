package com.piodev.habityfit.core.util

fun validateFormatDouble(input: String, max: Double): Boolean {
    if (input.isEmpty()) return true
    val isValid = input.count { it == '.' } <= 1 && input.all { it.isDigit() || it == '.' }
    val value = input.toDoubleOrNull() ?: 0.0
    return isValid && value <= max
}
fun validateFormatInt(input: String, max: Int): Boolean {
    if (input.isEmpty()) return true
    val isValidFormat = input.all { it.isDigit() }
    val value = input.toIntOrNull() ?: 0
    return isValidFormat && value <= max
}

fun normalizeInt(input: String, min: Int = 0, max: Int): String {
    if (input.isBlank()) return ""
    val value = input.toIntOrNull() ?: min
    return value.coerceIn(min, max).toString()
}

fun normalizeDouble(input: String, min: Double = 0.0, max: Double): String {
    if (input.isBlank()) return ""
    val value = input.toDoubleOrNull() ?: min
    return value.coerceIn(min, max).toString()
}

fun limitTextLength(input: String, maxLength: Int): String {
    return if (input.length > maxLength) {
        input.take(maxLength)
    } else {
        input
    }
}