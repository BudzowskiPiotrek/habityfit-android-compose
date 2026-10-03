package com.piodev.habityfit.features.routine.presentation.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.piodev.habityfit.features.exercises.domain.model.Exercise

import com.piodev.habityfit.features.routine.presentation.state.ExerciseConfigForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseAttachmentModal(
    showSheet: Boolean,
    sheetState: SheetState,
    searchQuery: String,
    searchResults: List<Exercise>,
    isSearching: Boolean,
    selectedExerciseForConfig: Exercise?,
    configForm: ExerciseConfigForm,
    onQueryChange: (String) -> Unit,
    onExerciseSelected: (Exercise) -> Unit,
    onConfigFormChange: (ExerciseConfigForm) -> Unit,
    onConfirm: (Int, Int, Int, Double) -> Unit,
    onDismiss: () -> Unit
) {
    if (!showSheet) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        windowInsets = WindowInsets.ime
    ) {
        if (selectedExerciseForConfig != null) {
            ExerciseConfigContent(
                exerciseName = selectedExerciseForConfig.name,
                form = configForm,
                onValueChange = onConfigFormChange,
                onConfirm = onConfirm,
                onCancel = onDismiss
            )
        } else {
            ExerciseSearchContent(
                query = searchQuery,
                onQueryChange = onQueryChange,
                results = searchResults,
                isSearching = isSearching,
                onExerciseSelected = onExerciseSelected
            )
        }
    }
}