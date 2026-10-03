package com.piodev.habityfit.features.informe.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.features.informe.domain.model.InformePeriod

@Composable
fun InformePeriodSelector(
    selected: InformePeriod,
    onSelected: (InformePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val row1 = listOf(
        InformePeriod.WEEK to stringResource(R.string.informe_period_week),
        InformePeriod.MONTH to stringResource(R.string.informe_period_month)
    )
    val row2 = listOf(
        InformePeriod.LAST_3_MONTHS to stringResource(R.string.informe_period_3_months),
        InformePeriod.YEAR to stringResource(R.string.informe_period_year)
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .selectableGroup()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                row1.forEach { (period, label) ->
                    PeriodOption(
                        label = label,
                        isSelected = selected == period,
                        onClick = { onSelected(period) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                row2.forEach { (period, label) ->
                    PeriodOption(
                        label = label,
                        isSelected = selected == period,
                        onClick = { onSelected(period) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}