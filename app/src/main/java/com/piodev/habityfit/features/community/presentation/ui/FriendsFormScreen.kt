package com.piodev.habityfit.features.community.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.core.componets.SetpointFormScaffold
import com.piodev.habityfit.features.community.presentation.viewmodel.FriendsViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.piodev.habityfit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendFormScreen(
    onBack: () -> Unit,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var selectedTab by remember { mutableIntStateOf(1) }
    val isExistingUser = selectedTab == 0

    SetpointFormScaffold(
        title = if (isExistingUser)
            stringResource(R.string.friends_form_exists_title)
        else
            stringResource(R.string.friends_form_new_title),
        onBack = onBack,
        onSave = {
            if (isExistingUser) viewModel.sendFriendRequest()
            else viewModel.sendInvitation()
        },
        canSave = state.friendCodeInput.isNotBlank() && state.friendCodeError == null && !state.isLoading,
        isLoading = state.isLoading,
        descriptionInfo = if (isExistingUser)
            stringResource(R.string.friends_form_exists_confirmation_text)
        else
            stringResource(R.string.friends_form_new_confirmation_text)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        viewModel.onFriendCodeChange("")
                    },
                    text = { Text(stringResource(R.string.friends_form_exists_tab_text)) },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        viewModel.onFriendCodeChange("")
                    },
                    text = { Text(stringResource(R.string.friends_form_new_tab_text)) },
                    selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    imageVector = if (isExistingUser)
                        Icons.Default.AccountCircle
                    else
                        Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = if (isExistingUser)
                        stringResource(R.string.friends_form_exists_body_text)
                    else
                        stringResource(R.string.friends_form_new_body_text),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.friendCodeInput,
                    onValueChange = { viewModel.onFriendCodeChange(it, !isExistingUser) },
                    label = {
                        Text(
                            if (isExistingUser)
                                stringResource(R.string.friends_form_friend_exists_label)
                            else
                                stringResource(R.string.friends_form_friend_new_label)
                        )
                    },
                    placeholder = {
                        Text(
                            if (isExistingUser)
                                stringResource(R.string.friends_form_friend_exists_ph)
                            else
                                stringResource(R.string.friends_form_friend_new_ph)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = if (isExistingUser) Icons.Default.AccountCircle else Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    isError = state.friendCodeError != null,
                    supportingText = state.friendCodeError?.let {
                        {
                            Text(
                                it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (isExistingUser) KeyboardType.Text else KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )

                if (state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}