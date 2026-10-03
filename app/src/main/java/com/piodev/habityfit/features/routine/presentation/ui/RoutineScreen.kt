package com.piodev.habityfit.features.routine.presentation.ui

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.features.routine.presentation.viewmodel.RoutineViewModel
import com.piodev.habityfit.features.routine.presentation.ui.components.RoutineSelectionItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.res.stringResource
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.AlertModalButton
import com.piodev.habityfit.core.componets.AlertModalInfo
import com.piodev.habityfit.core.componets.FullScreenBlurBlocker
import com.piodev.habityfit.features.routine.presentation.ui.components.FriendsSearchContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineScreen(
    onAddRoutine: () -> Unit,
    onRoutineClick: (String) -> Unit,
    onEditRoutine: (String) -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    GenericScreen(
        title = stringResource(R.string.routine_title_text),
        items = state.routines,
        itemKey = { it.id },
        emptyMessage = stringResource(R.string.routine_no_data_text),

        enableSwipeActions = true,
        onAddClick = onAddRoutine,
        onDelete = { viewModel.deleteRoutine(it) },
        onEdit = { onEditRoutine(it) },
        actions = { /* acciones adicionales si es necesario */ },

        itemContent = { r ->
            RoutineSelectionItem(
                routine = r,
                onClick = { onRoutineClick(r.id) },
                onSendClick = { viewModel.onPendingRoutineId(r.id) }
            )
        }
    )

    if (state.pendingRoutineId != null && state.pendingRequest == null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissAll() },
            sheetState = sheetState
        ) {
            FriendsSearchContent(
                friends = state.friends,
                onFriendSelected = { viewModel.onPendingRequest(it) }
            )
        }
    }

    FullScreenBlurBlocker(
        isVisible = state.pendingRequest != null,
        modalContent = {
            AlertModalInfo(
                description = stringResource(
                    R.string.routine_description_Alert_modal,
                    state.friends.find { it.friend.id == state.pendingRequest?.targetUserId }
                        ?.friend?.email ?: "")
            )
        },
        actionButtons = {
            AlertModalButton(
                confirmText = stringResource(R.string.send),
                cancelText = stringResource(R.string.cancel),
                onConfirm = { viewModel.onConfirmSend() },
                onCancel = { viewModel.dismissAll() }
            )
        }
    )
}
