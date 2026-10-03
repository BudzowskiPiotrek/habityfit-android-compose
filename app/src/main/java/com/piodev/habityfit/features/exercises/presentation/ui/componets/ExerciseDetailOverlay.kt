package com.piodev.habityfit.features.exercises.presentation.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.graphics.domain.model.ExerciseProgressSeries
import com.piodev.habityfit.features.graphics.domain.model.ProgressMetric
import com.piodev.habityfit.features.graphics.presentation.state.ExerciseSeriesUiItem
import com.piodev.habityfit.features.graphics.presentation.ui.components.ExerciseProgressChart

@Composable
fun ExerciseDetailOverlay(
    exercise: Exercise,
    series: ExerciseProgressSeries?,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    ExercisePatternViewer(
                        imageUrl = exercise.imageUrl,
                        onClose = onClose
                    )
                }
            }

            if (!exercise.description.isNullOrBlank()) {
                item {
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }

            if (series != null && series.dataPoints.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.exercises_detail_est_weight_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    ExerciseProgressChart(
                        items = listOf(ExerciseSeriesUiItem(series = series, isVisible = true)),
                        allItems = listOf(ExerciseSeriesUiItem(series = series, isVisible = true)),
                        metric = ProgressMetric.ONE_RM,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.exercises_detail_max_weight_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    ExerciseProgressChart(
                        items = listOf(ExerciseSeriesUiItem(series = series, isVisible = true)),
                        allItems = listOf(ExerciseSeriesUiItem(series = series, isVisible = true)),
                        metric = ProgressMetric.MAX_WEIGHT,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}