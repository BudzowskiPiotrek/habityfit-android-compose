package com.piodev.habityfit.core.util

import java.text.Normalizer
import java.util.regex.Pattern

private val REGEX_UNACCENT = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")

fun String.normalizeForSearch(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.matcher(normalized).replaceAll("").lowercase()
}