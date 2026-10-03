package com.piodev.habityfit.features.community.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.BotonExtra
import com.piodev.habityfit.core.componets.GenericScreen
import com.piodev.habityfit.core.model.BotonExtraData
import com.piodev.habityfit.features.community.presentation.ui.componets.FriendItem

import com.piodev.habityfit.features.community.presentation.viewmodel.FriendsViewModel

@Composable
fun FriendsScreen(
    onAddFriend: () -> Unit,
    onSentRequests: () -> Unit,
    onRequests: () -> Unit,
    onBack: () -> Unit,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val opcionesBotones = listOf(
        BotonExtraData(
            icon = Icons.Default.PersonAdd,
            onClick = onAddFriend
        ),
        BotonExtraData(
            icon = Icons.Default.Send,
            onClick = onSentRequests
        ),
        BotonExtraData(
            icon = Icons.Default.Drafts,
            onClick = onRequests
        )

    )
    Box(modifier = Modifier.fillMaxSize()) {
        GenericScreen(
            title = stringResource(R.string.friends_title_text),
            items = state.friends,
            itemKey = { it.id },
            emptyMessage = stringResource(R.string.friends_no_data_text),

            enableSwipeActions = false,
            onBack = onBack,
            onAddClick = null,
            onDelete = { /* para añadir soft delete */ },
            onEdit = { /* para Editar soft delete */ },
            actions = { /* acciones adicionales si es necesario */ },

            itemContent = { relation ->
                FriendItem(
                    friend = relation.friend,
                    onDeleteClick = { viewModel.removeFriend(relation.id)}
                )
            }
        )

        BotonExtra(
            opciones = opcionesBotones,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp)
        )
    }
}