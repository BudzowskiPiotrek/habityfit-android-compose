package com.piodev.habityfit.features.community.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.features.community.presentation.ui.componets.RoutineRequestItem
import com.piodev.habityfit.features.community.presentation.viewmodel.RoutineRequestViewModel

@Composable
fun RoutineRequestScreen(
    onBack: () -> Unit,
    viewModel: RoutineRequestViewModel = hiltViewModel()
) {
    val state = viewModel.state

    GenericScreen(
        title = stringResource(R.string.request_routine_title_text),
        items = state.requests,
        itemKey = { it.id },
        emptyMessage = stringResource(R.string.request_routine_no_data_text),
        enableSwipeActions = false,
        onBack = onBack,
        onAddClick = null,
        onDelete = {},
        onEdit = {},
        paddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        actions = {},
        itemContent = { request ->
            RoutineRequestItem(
                request = request,
                onAccept = { viewModel.acceptRequest(it) },
                onDecline = { viewModel.declineRequest(it) }
            )
        }
    )
}