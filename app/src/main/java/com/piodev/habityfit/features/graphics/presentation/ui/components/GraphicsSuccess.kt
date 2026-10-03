package com.piodev.habityfit.features.graphics.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import  androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.core.theme.LINE_COLORS
import com.piodev.habityfit.features.graphics.domain.model.ProgressMetric
import com.piodev.habityfit.features.graphics.presentation.state.ExerciseSeriesUiItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GraphicsSuccess(
    items: List<ExerciseSeriesUiItem>,
    onToggle: (String) -> Unit,
    paddingValues: PaddingValues
) {
    val visibleItems = items.filter { it.isVisible }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GraphicsSectionTitle(stringResource(R.string.graphics_rm1_title))

            if (visibleItems.isEmpty()) {
                EmptyChartPlaceholder(stringResource(R.string.graphics_no_data_exercise_text))
            } else {
                ExerciseProgressChart(
                    items = visibleItems,
                    allItems = items,
                    metric = ProgressMetric.ONE_RM,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }
        }
        item {
            GraphicsSectionTitle(stringResource(R.string.graphics_volume_title))

            if (visibleItems.isEmpty()) {
                EmptyChartPlaceholder(stringResource(R.string.graphics_no_data_exercise_text))
            } else {
                ExerciseProgressChart(
                    items = visibleItems,
                    allItems = items,
                    metric = ProgressMetric.VOLUME,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            GraphicsSectionTitle(stringResource(R.string.graphics_max_weight_title))

            if (visibleItems.isEmpty()) {
                EmptyChartPlaceholder(stringResource(R.string.graphics_no_data_exercise_text))
            } else {
                ExerciseProgressChart(
                    items = visibleItems,
                    allItems = items,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }
        }
        item {
            Text(
                text = stringResource(R.string.graphics_list_exercises_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            val uniqueItems = items.distinctBy { it.series.exerciseId }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 2
            ) {
                uniqueItems.forEachIndexed { index, item ->
                    val originalIndex = items.indexOfFirst { it.series.exerciseId == item.series.exerciseId }
                    val exerciseColor = LINE_COLORS[originalIndex % LINE_COLORS.size]

                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = item.isVisible,
                        onClick = { onToggle(item.series.exerciseId) },
                        label = {
                            Text(
                                text = item.series.exerciseName,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = exerciseColor.copy(alpha = 0.2f),
                            selectedLabelColor = exerciseColor,
                            selectedLeadingIconColor = exerciseColor,
                            disabledSelectedContainerColor = exerciseColor.copy(alpha = 0.1f)
                        ),
                        leadingIcon = if (item.isVisible) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}