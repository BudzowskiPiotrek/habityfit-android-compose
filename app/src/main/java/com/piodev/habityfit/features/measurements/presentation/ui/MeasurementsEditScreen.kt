package com.piodev.habityfit.features.measurements.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericEditFormScreen
import com.piodev.habityfit.core.config.LimitsConfig.MAX_HUMAN_FAT
import com.piodev.habityfit.core.config.LimitsConfig.MAX_HUMAN_MUSCLE
import com.piodev.habityfit.core.config.LimitsConfig.MAX_HUMAN_WATER
import com.piodev.habityfit.core.config.LimitsConfig.MAX_HUMAN_WEIGHT
import com.piodev.habityfit.core.model.EditableField
import com.piodev.habityfit.features.measurements.presentation.viewmodel.MeasurementsFormViewModel


@Composable
fun MeasurementsEditScreen(
    bodyId: String,
    onBack: () -> Unit,
    viewModel: MeasurementsFormViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(bodyId) {
        viewModel.loadBodyData(bodyId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    val fields = listOf(
        EditableField.Decimal(
            key = "weight",
            label = stringResource(R.string.measurements_form_weight_label),
            value = state.weight,
            max = MAX_HUMAN_WEIGHT

        ),
        EditableField.Decimal(
            key = "muscle",
            label = stringResource(R.string.measurements_form_muscle_mass_label),
            value = state.muscleMass,
            isRequired = false,
            max= MAX_HUMAN_MUSCLE
        ),
        EditableField.Decimal(
            key = "fat",
            label = stringResource(R.string.measurements_form_fat_mass_label),
            value = state.fatMass,
            isRequired = false,
            max = MAX_HUMAN_FAT
        ),
        EditableField.Decimal(
            key = "water",
            label = stringResource(R.string.measurements_form_body_water_label),
            value = state.bodyWater,
            isRequired = false,
            max = MAX_HUMAN_WATER
        )
    )

    GenericEditFormScreen(
        title = stringResource(R.string.measurements_edit_title),
        fields = fields,
        isLoading = state.isLoading,
        onBack = onBack,
        onSave = { m -> viewModel.updateFromMap(m) }
    )
}