package com.piodev.habityfit.features.routine.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.piodev.habityfit.R


@Composable
fun ExerciseSearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<Exercise>,
    isSearching: Boolean,
    onExerciseSelected: (Exercise) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text(stringResource(R.string.routine_search_exercise_query_label)) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )

        if (isSearching) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(results) { exercise ->
                ListItem(
                    headlineContent = { Text(exercise.name) },
                    supportingContent = {
                        Text(exercise.equipment ?: stringResource(R.string.routine_search_exercise_equipment_text))
                    },
                    modifier = Modifier.clickable {
                        onExerciseSelected(exercise)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        headlineColor = MaterialTheme.colorScheme.onSurface,
                        supportingColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp
                )
            }
        }

        if (!isSearching && query.length > 2 && results.isEmpty()) {
            Text(
                text = stringResource(R.string.routine_search_exercise_no_found_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}