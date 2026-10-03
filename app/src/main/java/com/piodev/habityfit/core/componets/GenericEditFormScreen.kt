package com.piodev.habityfit.core.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.core.model.EditableField
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.piodev.habityfit.core.util.limitTextLength
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.core.util.normalizeInt

@Composable
fun GenericEditFormScreen(
    title: String,
    fields: List<EditableField>,
    onSave: (Map<String, String>) -> Unit,
    onBack: () -> Unit,
    isLoading: Boolean = false
) {
    val formValues = remember { mutableStateMapOf<String, String>() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(fields) {
        fields.forEach { field ->
            formValues[field.key] = field.initialValue
        }
    }

    SetpointFormScaffold(
        title = title,
        onBack = onBack,
        onSave = { onSave(formValues.toMap()) },
        canSave = fields.all { field ->
            !field.isRequired || formValues[field.key]?.isNotBlank() == true
        },
        isLoading = isLoading
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(fields, key = { it.key }) { field ->
                val currentValue = formValues[field.key] ?: ""

                OutlinedTextField(
                    value = currentValue,
                    onValueChange = { newValue ->
                        when (field) {
                            is EditableField.Number -> {
                                if (newValue.isEmpty()) {
                                    formValues[field.key] = newValue
                                } else {
                                    val normalizer =
                                        normalizeInt(input = newValue, max = field.max as Int)
                                    formValues[field.key] = normalizer
                                }
                            }

                            is EditableField.Decimal -> {
                                if (newValue.isEmpty() || newValue == ".") {
                                    formValues[field.key] = newValue
                                } else {
                                    val filtered =
                                        normalizeDouble(input = newValue, max = field.max as Double)
                                    formValues[field.key] = filtered
                                }
                            }

                            is EditableField.Text -> {
                                val filtered = if (field.max != null) {
                                    limitTextLength(newValue, field.max as Int)
                                } else {
                                    newValue
                                }
                                formValues[field.key] = filtered
                            }

                            is EditableField.TextArea -> {
                                val filtered = if (field.max != null) {
                                    limitTextLength(newValue, field.max as Int)
                                } else {
                                    newValue
                                }
                                formValues[field.key] = filtered
                            }
                        }
                    },
                    label = {
                        val labelText =
                            if (field.isRequired) field.label else "${field.label} (Opcional)"
                        Text(labelText)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = field !is EditableField.TextArea,
                    minLines = if (field is EditableField.TextArea) 4 else 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (field) {
                            is EditableField.Number -> KeyboardType.Number
                            is EditableField.Decimal -> KeyboardType.Decimal
                            else -> KeyboardType.Text
                        }
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = when (field) {
                                is EditableField.Decimal -> Icons.Default.Info
                                is EditableField.Number -> Icons.Default.Info
                                else -> Icons.Default.Edit
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}