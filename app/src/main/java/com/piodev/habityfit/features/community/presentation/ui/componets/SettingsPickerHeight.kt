package com.piodev.habityfit.features.community.presentation.ui.componets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPickerHeight(
    currentHeight: Double,
    onConfirm: (Double) -> Unit
) {
    var sliderValue by remember { mutableStateOf(currentHeight.toInt()) }

    Text(
        text = stringResource(R.string.settings_height_label),
        style = MaterialTheme.typography.titleMedium)

    Text(
        text = "$sliderValue cm",
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary
    )

    Slider(
        value = sliderValue.toFloat(),
        onValueChange = { sliderValue = it.toInt() },
        valueRange = 50f..250f,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = { onConfirm(sliderValue.toDouble()) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.confirm))
    }
}
