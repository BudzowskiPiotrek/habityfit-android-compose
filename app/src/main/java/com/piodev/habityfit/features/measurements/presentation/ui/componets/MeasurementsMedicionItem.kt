package com.piodev.habityfit.features.measurements.presentation.ui.componets


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.features.measurements.domain.model.Body
import java.text.SimpleDateFormat
import java.util.*
import com.piodev.habityfit.R

@Composable
fun MeasurementsMedicionItem(
    medicion: Body,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val formattedDate = dateFormatter.format(Date(medicion.date))

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${medicion.weight} kg",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                medicion.muscleMass?.let {
                    MeasurementsMetricChip(
                        label = stringResource(R.string.measurements_metric_muscle_text),
                        value = "$it kg",
                        modifier = Modifier.weight(1f)
                    )
                }
                medicion.fatMass?.let {
                    MeasurementsMetricChip(
                        label = stringResource(R.string.measurements_metric_fat_text),
                        value = "$it kg",
                        modifier = Modifier.weight(1f)
                    )
                }
                if (medicion.fatMass != null && medicion.weight > 0) {
                    val fatPercentage = (medicion.fatMass / medicion.weight) * 100
                    val formattedPercent = "%.1f".format(fatPercentage)

                    MeasurementsMetricChip(
                        label = stringResource(R.string.measurements_metric_fat_perc_text),
                        value = "$formattedPercent%",
                        modifier = Modifier.weight(1f)
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                medicion.bodyWater?.let {
                    MeasurementsMetricChip(
                        label = stringResource(R.string.measurements_metric_water_text),
                        value = "$it L",
                        modifier = Modifier.weight(1f)
                    )
                }

                if (medicion.fatMass != null && medicion.weight > 0) {
                    val leanBodyMass = medicion.weight - medicion.fatMass
                    val bmr = 370 + (21.6 * leanBodyMass)

                    MeasurementsMetricChip(
                        label = stringResource(R.string.measurements_metric_bmr_text),
                        value = "${bmr.toInt()} kcal",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

