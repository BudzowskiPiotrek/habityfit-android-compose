package com.piodev.habityfit.features.graphics.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.ContentEmpty
import com.piodev.habityfit.core.componets.ContentError
import com.piodev.habityfit.core.componets.ContentLoading
import com.piodev.habityfit.features.graphics.presentation.state.GraphicsLoadState
import com.piodev.habityfit.features.graphics.presentation.ui.components.GraphicsSuccess
import com.piodev.habityfit.features.graphics.presentation.viewmodel.GraphicsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphicsScreen(
    onBack: () -> Unit,
    viewModel: GraphicsViewModel = hiltViewModel()
) {
    val state = viewModel.state

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
                            text = stringResource(R.string.graphics_title_text),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }

    ) { innerPadding ->
        when (val loadState = state.loadState) {
            is GraphicsLoadState.Loading -> ContentLoading(innerPadding)
            is GraphicsLoadState.Empty -> ContentEmpty(
                text = stringResource(R.string.graphics_no_data_text),
                innerPadding
            )

            is GraphicsLoadState.Error -> ContentError(loadState.message, innerPadding)
            is GraphicsLoadState.Success -> GraphicsSuccess(
                items = loadState.items,
                onToggle = { viewModel.toggleExerciseVisibility(it) },
                paddingValues = innerPadding
            )
        }
    }
}