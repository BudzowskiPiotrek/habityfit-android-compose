package com.piodev.habityfit.features.workout.presentation.ui.componets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.features.workout.domain.model.WorkoutSet
import androidx.compose.ui.text.input.KeyboardType
import com.piodev.habityfit.features.workout.presentation.state.SetEvent

@Composable
fun SetEditorRow(
    set: WorkoutSet,
    onEvent: (SetEvent) -> Unit
) {
    var repsText by remember(set.id) { mutableStateOf(set.reps.toString()) }
    var weightText by remember(set.id) { mutableStateOf(set.weight?.toString() ?: "") }
    var rpeText by remember(set.id) { mutableStateOf(set.rpe?.toString() ?: "") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "S${set.setNumber}",
            modifier = Modifier.width(32.dp),
            fontWeight = FontWeight.Bold
        )

        // PESO
        OutlinedTextField(
            value = weightText,
            onValueChange = {
                weightText = it
                onEvent(SetEvent.OnWeightChanged(set.id, it))
            },
            label = { Text("Kg") },
            modifier = Modifier
                .weight(1.2f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        weightText = ""
                    }
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

        // REPS
        OutlinedTextField(
            value = repsText,
            onValueChange = {
                repsText = it
                onEvent(SetEvent.OnRepsChanged(set.id, it))
            },
            label = { Text("Reps/Seg") },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        repsText = ""
                    }
                },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // RPE
        OutlinedTextField(
            value = rpeText,
            onValueChange = {
                rpeText = it
                onEvent(SetEvent.OnRpeChanged(set.id, it))
            },
            label = { Text("RPE") },
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        rpeText = ""
                    }
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            placeholder = { "1-10" }
        )
    }
}