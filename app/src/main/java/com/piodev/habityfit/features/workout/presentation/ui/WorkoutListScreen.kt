package com.piodev.habityfit.features.workout.presentation.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.features.workout.presentation.ui.componets.RoutineDetailCard
import com.piodev.habityfit.features.workout.presentation.viewmodel.WorkoutListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    sessionId: String,
    onBack: () -> Unit,
    onExerciseClick: (String) -> Unit,
    viewModel: WorkoutListViewModel = hiltViewModel()
) {
    val session by viewModel.sessionState.collectAsStateWithLifecycle()
    val exercisesList = session?.exercises ?: emptyList()

    GenericScreen(
        title = stringResource(R.string.workout_list_title_text),
        items = exercisesList,
        itemKey = { it.id },
        emptyMessage = stringResource(R.string.workout_list_no_data_text),

        enableSwipeActions = false,
        onBack = onBack,
        onAddClick = null,
        onDelete = { /* para añadir soft delete */ },
        onEdit = { /* para Editar soft delete */ },
        actions = { /* acciones adicionales si es necesario */ },

        itemContent = { exercise ->

            RoutineDetailCard(
                workout = exercise,
                onClick = { onExerciseClick(exercise.id) }
            )
        }
    )
}



