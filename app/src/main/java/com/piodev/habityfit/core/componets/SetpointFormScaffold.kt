package com.piodev.habityfit.core.componets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.core.model.FormDialogType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetpointFormScaffold(
    title: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    canSave: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    descriptionInfo: String? = null,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var currentDialog by rememberSaveable { mutableStateOf(FormDialogType.NONE) }

    val dialogText = when (currentDialog) {
        FormDialogType.BACK -> stringResource(R.string.core_form_exit_confirm_text)
        FormDialogType.SAVE -> descriptionInfo
            ?: stringResource(R.string.core_form_save_confirm_text)

        FormDialogType.NONE -> ""
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(60.dp),
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { currentDialog = FormDialogType.BACK },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            currentDialog = FormDialogType.SAVE
                        },
                        modifier = Modifier.fillMaxHeight(),
                        enabled = canSave && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(R.string.save)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = floatingActionButton
    ) { padding ->
        content(padding)
    }
    //////////////////////////////////////////////////////////////////////////////////////// Dialogs
    FullScreenBlurBlocker(
        isVisible = currentDialog != FormDialogType.NONE,
        modalContent = {
            AlertModalInfo(
                description = dialogText
            )
        },
        actionButtons = {
            AlertModalButton(
                confirmText = stringResource(R.string.confirm),
                cancelText = stringResource(R.string.cancel),
                onConfirm = {
                    if (currentDialog == FormDialogType.BACK) onBack()
                    else onSave()
                    currentDialog = FormDialogType.NONE
                },
                onCancel = {
                    currentDialog = FormDialogType.NONE
                }
            )
        }
    )
}