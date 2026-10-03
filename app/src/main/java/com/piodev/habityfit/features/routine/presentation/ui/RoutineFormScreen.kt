package com.piodev.habityfit.features.routine.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.core.componets.SetpointFormScaffold
import com.piodev.habityfit.features.routine.presentation.ui.components.ExerciseAttachmentModal
import com.piodev.habityfit.features.routine.presentation.ui.components.ExerciseSelectionItem
import com.piodev.habityfit.features.routine.presentation.viewmodel.RoutineFormViewModel
import com.piodev.habityfit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFormScreen(
    viewModel: RoutineFormViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state = viewModel.state
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    SetpointFormScaffold(
        title = stringResource(R.string.routine_form_title_text),
        onBack = onBack,
        onSave = { viewModel.saveRoutine() },
        canSave = state.name.isNotBlank() && state.selectedExercises.isNotEmpty(),
        isLoading = state.isLoading,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.routine_form_add_cd)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text(stringResource(R.string.routine_form_name_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text(stringResource(R.string.routine_form_description_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.routine_form_body_text),
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.selectedExercises) { selected ->
                    ExerciseSelectionItem(selected = selected, onRemove = {
                        viewModel.removeExercise(selected.exerciseId)
                    })
                }
            }
        }
    }

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
            viewModel.confirmExercise(sets, reps, rest, target)
            showSheet = false
        }
    )
}