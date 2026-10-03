package com.piodev.habityfit.core.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.piodev.habityfit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericScreen(
    title: String,
    items: List<T>,
    itemKey: (T) -> String,
    emptyMessage: String,
    onAddClick: (() -> Unit)? = null,
    onDelete: (String) -> Unit,
    onEdit: (String) -> Unit = {},
    onBack: (() -> Unit)? = null,
    enableSwipeActions: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(10.dp),
    itemContent: @Composable (T) -> Unit
) {
    var itemKeyToDelete by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(60.dp),
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        actions()
                    }
                }
            )
        },
        floatingActionButton = {
            if (onAddClick != null) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        }
    ) { innerPadding ->
        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyMessage,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = 10.dp
                    ),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = itemKey) { item ->
                    if (enableSwipeActions) {
                        SwipeActions(
                            onRequireDeleteConfirmation = {
                                itemKeyToDelete = itemKey(item)
                            },
                            onEdit = { onEdit(itemKey(item)) },
                        ) {
                            itemContent(item)
                        }
                    } else {
                        itemContent(item)
                    }
                }
            }
        }

        FullScreenBlurBlocker(
            isVisible = itemKeyToDelete != null,
            modalContent = {
                AlertModalInfo(
                    description = stringResource(R.string.core_generic_delete_confirm_text),
                )
            },
            actionButtons = {
                AlertModalButton(
                    confirmText = stringResource(R.string.eliminate),
                    cancelText = stringResource(R.string.cancel),
                    isDestructive = true,
                    onConfirm = {
                        itemKeyToDelete?.let { onDelete(it) }
                        itemKeyToDelete = null
                    },
                    onCancel = {
                        itemKeyToDelete = null
                    }
                )
            }
        )
    }
}