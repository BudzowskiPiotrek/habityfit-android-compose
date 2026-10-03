package com.piodev.habityfit.features.workout.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericEditFormScreen
import com.piodev.habityfit.core.config.LimitsConfig.MAX_WORKOUT_DURATION
import com.piodev.habityfit.core.config.LimitsConfig.MAX_WORKOUT_NOTES
import com.piodev.habityfit.core.model.EditableField
import com.piodev.habityfit.features.workout.presentation.viewmodel.WorkoutFormViewModel

@Composable
fun WorkoutEditScreen(
    sessionId: String,
    onBack: () -> Unit,
    viewModel: WorkoutFormViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(sessionId) {
        viewModel.loadWorkout(sessionId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    val fields = listOf(
        EditableField.Number(
            key = "duration",
            label = stringResource(R.string.workout_form_duration_label),
            value = state.durationMinutes,
            max = MAX_WORKOUT_DURATION
        ),
        EditableField.TextArea(
            key = "notes",
            label = stringResource(R.string.workout_form_notes_label),
            value = state.notes,
            isRequired = false,
            max = MAX_WORKOUT_NOTES
        )
    )

    GenericEditFormScreen(
        title = stringResource(R.string.workout_edit_title_text),
        fields = fields,
        isLoading = state.isLoading,
        onBack = onBack,
        onSave = { m -> viewModel.updateFromMap(m) }
    )
}