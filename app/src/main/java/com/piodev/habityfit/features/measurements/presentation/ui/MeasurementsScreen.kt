package com.piodev.habityfit.features.measurements.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.features.measurements.presentation.ui.componets.MeasurementsMedicionItem
import com.piodev.habityfit.features.measurements.presentation.viewmodel.MeasurementsViewModel

@Composable
fun MeasurementsScreen(
    onAddMeasurement: () -> Unit,
    onEditMeasurement: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: MeasurementsViewModel = hiltViewModel()
) {
    val measurements by viewModel.measurements.collectAsStateWithLifecycle()

    GenericScreen(
        title = stringResource(R.string.measurements_title_text),
        items = measurements,
        itemKey = { it.id },
        emptyMessage = stringResource(R.string.measurements_no_data_text),

        enableSwipeActions = true,
        onBack = onBack,
        onAddClick = onAddMeasurement,
        onDelete = { viewModel.deleteBody(it) },
        onEdit = { onEditMeasurement(it) },
        actions = { /* acciones adicionales si es necesario */ },

        itemContent = { m ->
            MeasurementsMedicionItem(
                medicion = m
            )
        }
    )
}