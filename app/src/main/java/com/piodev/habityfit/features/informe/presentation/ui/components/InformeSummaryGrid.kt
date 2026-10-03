package com.piodev.habityfit.features.informe.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.features.informe.domain.model.InformeSummary

@Composable
fun InformeSummaryGrid(
    summary: InformeSummary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InformeSummaryCard(
                label = stringResource(R.string.informe_summary_workouts),
                value = summary.totalWorkouts.toString(),
                modifier = Modifier.weight(1f)
            )
            InformeSummaryCard(
                label = stringResource(R.string.informe_summary_sets),
                value = summary.totalSets.toString(),
                modifier = Modifier.weight(1f)
            )
                    }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InformeSummaryCard(
                label = stringResource(R.string.informe_summary_volume),
                value = "%.1f t".format(summary.totalVolume / 1000.0),
                modifier = Modifier.weight(1f)
            )
            InformeSummaryCard(
                label = stringResource(R.string.informe_summary_duration),
                value = "${summary.totalDurationMinutes} min",
                modifier = Modifier.weight(1f)
            )
        }
    }
}