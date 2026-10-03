package com.piodev.habityfit.features.workout.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.features.workout.presentation.ui.componets.WorkoutDetailCard
import com.piodev.habityfit.features.workout.presentation.viewmodel.WorkoutViewModel

@Composable
fun WorkoutScreen(
    onAddWorkout: () -> Unit,
    onSessionClick: (String) -> Unit,
    onEditSession: (String) -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val sessions by viewModel.session.collectAsStateWithLifecycle()

    GenericScreen(
        title = stringResource(R.string.workout_title_text),
        items = sessions,
        itemKey = { it.id },
        emptyMessage = stringResource(R.string.workout_no_data_text),

        enableSwipeActions = true,
        onAddClick = onAddWorkout,
        onDelete = { viewModel.deleteWorkout(it) },
        onEdit = { onEditSession(it) },
        actions = { /* acciones adicionales si es necesario */ },

        itemContent = { s ->
            WorkoutDetailCard(
                session = s,
                onClick = { onSessionClick(s.id) },
                onFinishSessionClick = { viewModel.updateWorkoutDuration(s.id) }
            )
        }
    )
}