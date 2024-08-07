package com.susieson.anchor.ui.components

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.theme.AnchorTheme

const val MinPasswordLength = 6

@Composable
fun LoginForm(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    isEmailEnabled: Boolean = true,
    submitButtonText: @Composable () -> Unit
) {
    val isEmailError = email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordError = password.isNotEmpty() && password.length < MinPasswordLength
    val isFormValid = email.isNotEmpty() && password.isNotEmpty() && !isEmailError && !isPasswordError

    Column(modifier) {
        EmailTextField(
            email = email,
            isError = isEmailError,
            enabled = isEmailEnabled,
            onEmailChange = onEmailChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        PasswordTextField(
            password = password,
            isError = isPasswordError,
            isPasswordVisible = isPasswordVisible,
            onPasswordChange = onPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier.fillMaxWidth()
        )
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Button(
            onClick = onSubmit,
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            submitButtonText()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    AnchorTheme {
        LoginForm(
            email = "email@example.com",
            password = "123",
            isPasswordVisible = false,
            errorMessage = "There was an error logging in. Please try again.",
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onSubmit = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login_button))
        }
    }
}
