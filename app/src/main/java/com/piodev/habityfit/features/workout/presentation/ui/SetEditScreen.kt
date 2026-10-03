package com.piodev.habityfit.features.workout.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.features.workout.presentation.viewmodel.SetEditViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.features.workout.presentation.ui.componets.SetEditorRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.piodev.habityfit.R
import com.piodev.habityfit.features.exercises.presentation.ui.componets.ExercisePatternViewer
import com.piodev.habityfit.features.workout.presentation.state.SetEvent
import com.piodev.habityfit.features.workout.presentation.ui.componets.RestTimerOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetEditScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: SetEditViewModel = hiltViewModel()
) {
    val sets by viewModel.setsState.collectAsStateWithLifecycle()
    val timerValue by viewModel.timerValue.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val exercise by viewModel.exercise.collectAsStateWithLifecycle()

    LaunchedEffect(exerciseId) {
        viewModel.loadSets(exerciseId)
    }

    Scaffold(
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
                            text = stringResource(R.string.workout_edit_sets_title_text),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(SetEvent.OnStartTimer(exerciseId)) },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessAlarms,
                            contentDescription = null
                        )
                    }

                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
                .padding(padding),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .padding(horizontal = 0.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        // En futuro pondremos aqui algo
                    }
                    ExercisePatternViewer(
                        imageUrl = exercise?.imageUrl,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .alpha(0.9f)
                    )
                }
            }
            items(sets, key = { it.id }) { set ->
                SetEditorRow(
                    set = set,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
    if (timerValue > 0) {
        RestTimerOverlay(
            timeLeft = timerValue,
            onDismiss = { viewModel.cancelTimer() }
        )
    }
}
