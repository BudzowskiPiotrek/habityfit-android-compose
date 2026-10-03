package com.piodev.habityfit.features.auth.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.R
import com.piodev.habityfit.features.auth.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    inviteToken: String? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        errorContainerColor = MaterialTheme.colorScheme.surface,
        focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
        errorBorderColor = MaterialTheme.colorScheme.error,
        focusedLabelColor = MaterialTheme.colorScheme.onError,
        unfocusedLabelColor = MaterialTheme.colorScheme.onError,
        errorLabelColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error
    )

    LaunchedEffect(inviteToken) {
        inviteToken?.let { viewModel.initWithToken(it) }
    }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.auth_title_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(32.dp))

            if (uiState.isInviteMode) {
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { viewModel.onFullNameChange(it) },
                    label = { Text(stringResource(R.string.auth_fullname_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors,
                    isError = uiState.fullNameError != null,
                    supportingText = {
                        if (uiState.fullNameError != null)
                            Text(uiState.fullNameError!!, color = MaterialTheme.colorScheme.error)
                        else
                            Text(
                                stringResource(R.string.auth_fullname_st),
                                color = MaterialTheme.colorScheme.onError
                            )
                    }
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text(stringResource(R.string.auth_password_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = fieldColors,
                    isError = uiState.passwordError != null,
                    supportingText = {
                        if (uiState.passwordError != null)
                            Text(uiState.passwordError!!, color = MaterialTheme.colorScheme.error)
                        else
                            Text(
                                stringResource(R.string.auth_password_st),
                                color = MaterialTheme.colorScheme.onError
                            )
                    }
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.passwordRepeat,
                    onValueChange = { viewModel.onPasswordRepeatChange(it) },
                    label = { Text(stringResource(R.string.auth_password_repeat_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = fieldColors,
                    isError = uiState.passwordRepeatError != null,
                    supportingText = {
                        if (uiState.passwordRepeatError != null)
                            Text(
                                uiState.passwordRepeatError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        else
                            Text(
                                stringResource(R.string.auth_password_repeat_st),
                                color = MaterialTheme.colorScheme.onError
                            )
                    }
                )
            } else {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text(stringResource(R.string.auth_email_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.emailError != null,
                    supportingText = {
                        if (uiState.emailError != null)
                            Text(uiState.emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text(stringResource(R.string.auth_password_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = fieldColors,
                )
            }

            uiState.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { if (uiState.isInviteMode) viewModel.acceptInvite() else viewModel.login() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                else
                    Text(
                        if (uiState.isInviteMode) stringResource(R.string.auth_create_button)
                        else stringResource(R.string.auth_enter_button)
                    )
            }

            if (!uiState.isInviteMode) {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text(stringResource(R.string.auth_account_button))
                }
            }
        }
    }
}