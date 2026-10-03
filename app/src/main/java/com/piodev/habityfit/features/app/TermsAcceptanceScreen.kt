package com.piodev.habityfit.features.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.R


import com.piodev.habityfit.features.app.viewmodel.TermsViewModel

@Composable
fun TermsAcceptanceScreen(
    onAccept: () -> Unit,
    viewModel: TermsViewModel = hiltViewModel()
) {
    var hasRead by remember { mutableStateOf(false) }

    val sections = listOf(
        Pair(R.string.terms_section_ai_title, R.string.terms_section_ai_body),
        Pair(R.string.terms_section_conduct_title, R.string.terms_section_conduct_body),
        Pair(R.string.terms_section_data_title, R.string.terms_section_data_body),
        Pair(R.string.terms_section_usage_title, R.string.terms_section_usage_body),
        Pair(R.string.terms_section_routines_title, R.string.terms_section_routines_body),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.terms_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                sections.forEach { (titleRes, bodyRes) ->
                    Text(
                        text = stringResource(titleRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    Text(
                        text = stringResource(bodyRes),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!hasRead) {
                Button(
                    onClick = { hasRead = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.terms_button_read))
                }
            } else {
                Button(
                    onClick = { viewModel.acceptTerms(onAccept) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.accept))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}