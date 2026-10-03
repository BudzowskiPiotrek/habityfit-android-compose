package com.piodev.habityfit.features.community.presentation.ui.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPickerDate(
    initialDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate ?: System.currentTimeMillis()
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f)
        .wrapContentHeight()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        DatePicker(
            state = datePickerState,
            title = null
        )
        TextButton(
            onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.accept))
        }
    }
}