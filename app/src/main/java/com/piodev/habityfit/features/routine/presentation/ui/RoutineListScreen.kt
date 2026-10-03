package com.piodev.habityfit.features.routine.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.features.routine.presentation.ui.components.ExerciseAttachmentModal
import com.piodev.habityfit.features.routine.presentation.ui.components.ExerciseDetailCard
import com.piodev.habityfit.features.routine.presentation.viewmodel.RoutineListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    routineId: String,
    onBack: () -> Unit,
    onEditExercise: (String) -> Unit,
    onNavigateToGraphics: (String) -> Unit,
    viewModel: RoutineListViewModel = hiltViewModel()
) {
    val routine by viewModel.routineState.collectAsStateWithLifecycle()
    val exercisesList = routine?.exercises ?: emptyList()
    val screenTitle = routine?.name ?: stringResource(R.string.load)

    val state = viewModel.state
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    GenericScreen(
        title = screenTitle,
        items = exercisesList,
        itemKey = { it.id },
        emptyMessage = stringResource(R.string.routine_list_no_data_text),

        enableSwipeActions = true,
        onBack = onBack,
        onAddClick = { showSheet = true },
        onDelete = { viewModel.deleteExercise(it) },
        onEdit = { onEditExercise(it) },
        actions = {
            IconButton(onClick = { onNavigateToGraphics(routineId) }) {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = stringResource(R.string.routine_list_progres_icon_cd)
                )
            }
        },

        itemContent = { exercise ->
            ExerciseDetailCard(exercise)
        }
    )

    ExerciseAttachmentModal(
        showSheet = showSheet,
        sheetState = sheetState,
        searchQuery = state.searchQuery,
        searchResults = state.searchResults,
        isSearching = state.isSearching,
        selectedExerciseForConfig = state.selectedExerciseForConfig,
        configForm = state.configForm,
        onQueryChange = { viewModel.onSearchQueryChange(it) },
        onExerciseSelected = { viewModel.onExerciseSelectedForConfig(it) },
        onConfigFormChange = { viewModel.onConfigFormChange(it) },
        onDismiss = {
            showSheet = false
            viewModel.cancelConfig()
        },
        onConfirm = { sets, reps, rest, target ->
            viewModel.addNewExercise(sets, reps, rest, target)
            showSheet = false
        }
    )
}