package com.piodev.habityfit.features.routine.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericEditFormScreen
import com.piodev.habityfit.core.config.LimitsConfig.MAX_EXERCISE_WEIGHT
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REST_SECONDS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SETS
import com.piodev.habityfit.core.model.EditableField
import com.piodev.habityfit.features.routine.presentation.viewmodel.ExerciseEditViewModel

@Composable
fun ExerciseEditScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ExerciseEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    val fields = listOf(
        EditableField.Number(
            key = "sets",
            label = stringResource(R.string.routine_form_sets_label),
            value = state.sets,
            max = MAX_SETS
        ),
        EditableField.Number(
            key = "reps",
            label = stringResource(R.string.routine_form_reps_label),
            value = state.reps,
            max = MAX_REPS
        ),
        EditableField.Number(
            key = "restSecond",
            label = stringResource(R.string.routine_form_rest_label),
            value = state.restSecond,
            max = MAX_REST_SECONDS
        ),
        EditableField.Decimal(
            key = "targetWeight",
            label = stringResource(R.string.routine_form_target_label),
            value = state.targetWeight,
            max = MAX_EXERCISE_WEIGHT
        ),
    )

    GenericEditFormScreen(
        title = stringResource(R.string.routine_edit_exercise_title_text),
        fields = fields,
        isLoading = state.isLoading,
        onBack = onBack,
        onSave = { m -> viewModel.updateFromMap(m) }
    )
}

