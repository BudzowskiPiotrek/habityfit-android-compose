package com.piodev.habityfit.features.exercises.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.features.exercises.presentation.ui.componets.BodySvg
import com.piodev.habityfit.features.exercises.presentation.ui.componets.ExerciseCard
import com.piodev.habityfit.features.exercises.presentation.ui.componets.ExerciseDetailOverlay
import com.piodev.habityfit.features.exercises.presentation.viewmodel.ExercisesViewModel

@Composable
fun ExercisesScreen(
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
            .padding(10.dp)
    ) {
        BodySvg(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f)
                .align(Alignment.CenterHorizontally),
            selectedMuscleId = state.selectedMuscleGroupId,
            onMuscleClick = { id -> viewModel.onMuscleSelected(id) }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                // TODO: Implement a dropdown menu for categories
            }
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    label = { Text(stringResource(R.string.exercises_search_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        if (state.exercises.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.exercises_no_data_text))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.exercises, key = { it.id }) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onClick = { viewModel.onExerciseSelected(exercise.id) }
                    )
                }
            }
        }
    }

    if (state.selectedExercise != null) {
        ExerciseDetailOverlay(
            exercise = state.selectedExercise,
            series = state.exerciseSeries,
            onClose = { viewModel.onExerciseSelected(null) }
        )
    }
}