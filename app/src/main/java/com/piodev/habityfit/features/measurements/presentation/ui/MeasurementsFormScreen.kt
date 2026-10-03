package com.piodev.habityfit.features.measurements.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.SetpointFormScaffold
import com.piodev.habityfit.features.measurements.presentation.viewmodel.MeasurementsFormViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementsFormScreen(
    onBack: () -> Unit,
    viewModel: MeasurementsFormViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    SetpointFormScaffold(
        title = stringResource(R.string.measurements_form_title_text),
        onBack = onBack,
        onSave = { viewModel.saveBodyData() },
        canSave = state.weight.isNotBlank(),
        isLoading = state.isLoading
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.weight,
                onValueChange = { viewModel.onWeightChange(it) },
                label = { Text(stringResource(R.string.measurements_form_weight_label)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = state.muscleMass,
                onValueChange = { viewModel.onMuscleChange(it) },
                label = { Text(stringResource(R.string.measurements_form_muscle_mass_label)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = state.fatMass,
                onValueChange = { viewModel.onFatChange(it) },
                label = { Text(stringResource(R.string.measurements_form_fat_mass_label)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = state.bodyWater,
                onValueChange = { viewModel.onWaterChange(it) },
                label = { Text(stringResource(R.string.measurements_form_body_water_label)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
    }
}