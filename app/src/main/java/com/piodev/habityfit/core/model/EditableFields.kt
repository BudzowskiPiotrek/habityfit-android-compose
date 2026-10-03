package com.piodev.habityfit.core.model

sealed class EditableField(
    val key: String,
    val label: String,
    val initialValue: String,
    val isRequired: Boolean = true,
    val max: Any? = null

) {
    class Text(
        key: String,
        label: String,
        value: String,
        isRequired: Boolean = true,
        max: Int? = null
    ) :
        EditableField(key, label, value, isRequired, max)

    class TextArea(
        key: String,
        label: String,
        value: String,
        isRequired: Boolean = true,
        max: Int? = null
    ) :
        EditableField(key, label, value, isRequired, max)

    class Number(
        key: String,
        label: String,
        value: String,
        isRequired: Boolean = true,
        max: Int? = null,
    ) :
        EditableField(key, label, value, isRequired, max)

    class Decimal(
        key: String,
        label: String,
        value: String,
        isRequired: Boolean = true,
        max: Double? = null,
    ) :
        EditableField(key, label, value, isRequired, max)
}