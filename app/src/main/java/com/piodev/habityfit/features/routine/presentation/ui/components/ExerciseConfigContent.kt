package com.piodev.habityfit.features.routine.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.features.routine.presentation.state.ExerciseConfigForm

@Composable
fun ExerciseConfigContent(
    exerciseName: String,
    form: ExerciseConfigForm,
    onValueChange: (ExerciseConfigForm) -> Unit,
    onConfirm: (Int, Int, Int, Double) -> Unit,
    onCancel: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
    )
    {
        Text(
            text = stringResource(R.string.routine_exercise_detail_title_text)
                    + exerciseName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = form.rest,
                onValueChange = { onValueChange(form.copy(rest = it)) },
                label = { Text(stringResource(R.string.routine_exercise_detail_rest_label)) },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onValueChange(form.copy(rest = ""))
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = form.weight,
                onValueChange = { onValueChange(form.copy(weight = it)) },
                label = { Text(stringResource(R.string.routine_exercise_detail_weight_label)) },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onValueChange(form.copy(weight = ""))
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        Spacer(Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = form.sets,
                onValueChange = { onValueChange(form.copy(sets = it)) },
                label = { Text(stringResource(R.string.routine_exercise_detail_sets_label)) },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onValueChange(form.copy(sets = ""))
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = form.reps,
                onValueChange = { onValueChange(form.copy(reps = it)) },
                label = { Text(stringResource(R.string.routine_exercise_detail_reps_label)) },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onValueChange(form.copy(reps = ""))
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Button(
            onClick = {
                onConfirm(
                    form.sets.toIntOrNull() ?: 3,
                    form.reps.toIntOrNull() ?: 10,
                    form.rest.toIntOrNull() ?: 60,
                    form.weight.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.routine_exercise_detail_add_button_text))
        }

        Spacer(Modifier.height(20.dp))
    }
}