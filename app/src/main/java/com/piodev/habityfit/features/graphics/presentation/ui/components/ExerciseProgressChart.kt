package com.piodev.habityfit.features.graphics.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piodev.habityfit.core.theme.LINE_COLORS
import com.piodev.habityfit.features.graphics.domain.model.ProgressMetric
import com.piodev.habityfit.features.graphics.presentation.state.ExerciseSeriesUiItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExerciseProgressChart(
    items: List<ExerciseSeriesUiItem>,
    allItems: List<ExerciseSeriesUiItem>,
    metric: ProgressMetric = ProgressMetric.MAX_WEIGHT,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val onSurfaceVariant = MaterialTheme.colorScheme.onPrimaryContainer
    val outline = MaterialTheme.colorScheme.onSurface
    val dateFormatter = remember { SimpleDateFormat("MM/dd", Locale.getDefault()) }

    Canvas(modifier = modifier.background(Color.Transparent)) {
        val padLeft = 52.dp.toPx()
        val padRight = 16.dp.toPx()
        val padTop = 12.dp.toPx()
        val padBottom = 36.dp.toPx()

        val chartWidth = size.width - padLeft - padRight
        val chartHeight = size.height - padTop - padBottom

        val allPoints: List<Pair<Long, Double>> = items.flatMap { item ->
            item.series.dataPoints.map { dp ->
                dp.sessionDate to when (metric) {
                    ProgressMetric.MAX_WEIGHT -> dp.maxWeight
                    ProgressMetric.ONE_RM -> dp.estimatedOneRM
                    ProgressMetric.VOLUME -> dp.totalVolume
                }
            }
        }

        if (allPoints.isEmpty()) return@Canvas

        val minX = allPoints.minOf { it.first }.toDouble()
        val maxX = allPoints.maxOf { it.first }.toDouble()
        val minY = (allPoints.minOf { it.second } * 0.9).coerceAtLeast(0.0)
        val maxY = allPoints.maxOf { it.second } * 1.1

        val xRange = (maxX - minX).takeIf { it > 0 } ?: 1.0
        val yRange = (maxY - minY).takeIf { it > 0 } ?: 1.0

        val gridLines = 5
        val labelStyle = TextStyle(fontSize = 10.sp, color = onSurfaceVariant)

        for (i in 0..gridLines) {
            val fraction = i.toFloat() / gridLines
            val y = padTop + chartHeight * (1f - fraction)
            val value = minY + yRange * fraction
            val label =
                if (value >= 1000) "${"%.0f".format(value / 1000)}k" else "%.0f".format(value)

            drawLine(
                color = outline.copy(alpha = 0.5f),
                start = Offset(padLeft, y),
                end = Offset(padLeft + chartWidth, y),
                strokeWidth = 0.7.dp.toPx()
            )

            val measured = textMeasurer.measure(label, labelStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = label,
                topLeft = Offset(
                    x = padLeft - measured.size.width - 6.dp.toPx(),
                    y = y - measured.size.height / 2f
                ),
                style = labelStyle
            )
        }

        val allDates = allPoints.map { it.first }.distinct().sorted()
        val totalDates = allDates.size
        val indicesToShow = when {
            totalDates <= 5 -> allDates.indices.toList()
            else -> {
                val step = (totalDates - 1) / 4
                (0..4).map { (it * step).coerceAtMost(totalDates - 1) }
                    .plus(totalDates - 1)
                    .distinct()
                    .sorted()
            }
        }
        indicesToShow.forEach { idx ->
            val timestamp = allDates[idx]
            val xFraction =
                if (xRange == 0.0) 0.5f else ((timestamp.toDouble() - minX) / xRange).toFloat()
            val x = padLeft + chartWidth * xFraction
            val label = dateFormatter.format(Date(timestamp))
            val measured = textMeasurer.measure(label, labelStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = label,
                topLeft = Offset(
                    x = x - measured.size.width / 2f,
                    y = padTop + chartHeight + 8.dp.toPx()
                ),
                style = labelStyle
            )
        }

        items.forEach { uiItem ->
            val originalIndex = allItems.indexOfFirst { it.series.exerciseId == uiItem.series.exerciseId }
            val color = LINE_COLORS[originalIndex.coerceAtLeast(0) % LINE_COLORS.size]
            val points = uiItem.series.dataPoints
                .sortedBy { it.sessionDate }
                .map { dp ->
                    val yVal = when (metric) {
                        ProgressMetric.MAX_WEIGHT -> dp.maxWeight
                        ProgressMetric.ONE_RM -> dp.estimatedOneRM
                        ProgressMetric.VOLUME -> dp.totalVolume
                    }
                    val xFraction =
                        if (xRange == 0.0) 0.5f else ((dp.sessionDate.toDouble() - minX) / xRange).toFloat()
                    val yFraction = ((yVal - minY) / yRange).toFloat()
                    Offset(
                        x = padLeft + chartWidth * xFraction,
                        y = padTop + chartHeight * (1f - yFraction)
                    )
                }

            if (points.size < 2) {
                points.firstOrNull()?.let { pt ->
                    drawCircle(color = color, radius = 5.dp.toPx(), center = pt)
                }
                return@forEach
            }

            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            points.forEach { pt ->
                drawCircle(color = color, radius = 4.dp.toPx(), center = pt)
                drawCircle(
                    color = Color.Black.copy(alpha = 0.6f),
                    radius = 4.dp.toPx(),
                    center = pt,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }
    }
}