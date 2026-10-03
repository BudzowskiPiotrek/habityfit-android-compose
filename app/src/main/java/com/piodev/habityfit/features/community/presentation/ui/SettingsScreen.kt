package com.piodev.habityfit.features.community.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.piodev.habityfit.features.community.presentation.viewmodel.SettingsViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.piodev.habityfit.R
import com.piodev.habityfit.core.componets.SetpointFormScaffold
import com.piodev.habityfit.features.community.domain.model.SettingsField
import com.piodev.habityfit.features.community.presentation.ui.componets.SettingsAthleteIdCard
import com.piodev.habityfit.features.community.presentation.ui.componets.SettingsInfoRow
import com.piodev.habityfit.features.community.presentation.ui.componets.SettingsPickerDate
import com.piodev.habityfit.features.community.presentation.ui.componets.SettingsPickerHeight
import com.piodev.habityfit.features.community.presentation.ui.componets.SettingsPickerSex
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var editingField by remember { mutableStateOf<SettingsField?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    SetpointFormScaffold(
        title = stringResource(R.string.settings_title_text),
        onBack = onBack,
        onSave = { viewModel.saveProfile() },
        canSave = !state.isLoading,
        isLoading = state.isLoading,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                var editingName by remember { mutableStateOf(false) }
                var nameInput by remember(state.fullName) { mutableStateOf(state.fullName) }

                if (editingName) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(stringResource(R.string.settings_name_input_label)) },
                        isError = nameInput.isBlank(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (nameInput.isNotBlank()) {
                                viewModel.onNameChange(nameInput)
                                editingName = false
                            }
                        }),
                        trailingIcon = {
                            if (nameInput.isNotBlank()) {
                                IconButton(onClick = {
                                    viewModel.onNameChange(nameInput)
                                    editingName = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = state.fullName.ifBlank { stringResource(R.string.settings_fullname_text) },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable {
                            editingName = true
                        }
                    )
                }

                Text(
                    text = state.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(32.dp))

                SettingsAthleteIdCard(userId = state.id)

                Spacer(Modifier.height(40.dp))


                SettingsInfoRow(
                    label = stringResource(R.string.settings_sex_label),
                    value = state.sex?.name ?: stringResource(R.string.settings_sex_value),
                    icon = Icons.Default.AccountCircle,
                    modifier = Modifier.clickable {
                        editingField = SettingsField.SEX
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)

                SettingsInfoRow(
                    label = stringResource(R.string.settings_height_label),
                    value = "${state.height ?: "--"} cm",
                    icon = Icons.Default.AccountBox,
                    modifier = Modifier.clickable {
                        editingField = SettingsField.HEIGHT
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)

                SettingsInfoRow(
                    label = stringResource(R.string.settings_birth_label),
                    value = state.birthDate?.let {
                        java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(Date(it))
                    } ?: "--",
                    icon = Icons.Default.DateRange,
                    modifier = Modifier.clickable {
                        editingField = SettingsField.BIRTH_DATE
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = {
                        viewModel.deactivateAccount()
                        Toast.makeText(
                            context,
                            context.getString(R.string.core_deactivate_toast_text),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.padding(bottom = 16.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Clear, null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.settings_desactivate_text_button),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (editingField != null) {
                ModalBottomSheet(
                    onDismissRequest = { editingField = null },
                    sheetState = sheetState
                ) {
                    when (editingField) {
                        SettingsField.SEX -> SettingsPickerSex(
                            selected = state.sex,
                            onSelected = {
                                viewModel.onSexChange(it)
                                editingField = null
                            }
                        )

                        SettingsField.HEIGHT -> SettingsPickerHeight(
                            currentHeight = state.height ?: 170.0,
                            onConfirm = {
                                viewModel.onHeightChange(it)
                                editingField = null
                            }
                        )

                        SettingsField.BIRTH_DATE -> SettingsPickerDate(
                            initialDate = state.birthDate,
                            onDateSelected = {
                                viewModel.onBirthDateChange(it)
                                editingField = null
                            }
                        )

                        null -> {}
                    }
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}