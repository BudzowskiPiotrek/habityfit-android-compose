package com.piodev.habityfit.features.routine.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericEditFormScreen
import com.piodev.habityfit.core.config.LimitsConfig.MAX_ROUTINE_DESC
import com.piodev.habityfit.core.config.LimitsConfig.MAX_ROUTINE_NAME
import com.piodev.habityfit.core.model.EditableField
import com.piodev.habityfit.features.routine.presentation.viewmodel.RoutineFormViewModel

@Composable
fun RoutineEditScreen(
    routineId: String,
    onBack: () -> Unit,
    viewModel: RoutineFormViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(routineId) {
        viewModel.loadRoutine(routineId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    val fields = listOf(
        EditableField.Text(
            key = "name",
            label = stringResource(R.string.routine_form_name_label),
            value = state.name,
            max = MAX_ROUTINE_NAME
        ),
        EditableField.TextArea(
            key = "description",
            label = stringResource(R.string.routine_form_description_label),
            value = state.description,
            isRequired = false,
            max = MAX_ROUTINE_DESC
        )
    )

    GenericEditFormScreen(
        title = stringResource(R.string.routine_edit_title_text),
        fields = fields,
        isLoading = state.isLoading,
        onBack = onBack,
        onSave = { m -> viewModel.updateFromMap(m) }
    )
}