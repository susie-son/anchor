package com.susieson.anchor.ui.components

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    passwordVisible: Boolean,
    error: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    onSubmit: () -> Unit,
    submit: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isEmailEnabled: Boolean = true
) {
    val emailError = email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val passwordError = password.isNotEmpty() && password.length < MinPasswordLength
    val isValid = email.isNotEmpty() && password.isNotEmpty() && !emailError && !passwordError

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        EmailTextField(
            email = email,
            isError = emailError,
            enabled = isEmailEnabled,
            onEmailChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        PasswordTextField(
            password = password,
            error = passwordError,
            isPasswordVisible = passwordVisible,
            onPasswordChange = onPasswordChange,
            onPasswordVisibleChange = onPasswordVisibleChange,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(
                error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }
        Button(
            onClick = onSubmit,
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            submit()
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
            passwordVisible = false,
            error = "There was an error logging in. Please try again.",
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibleChange = {},
            onSubmit = {},
            submit = { Text(stringResource(R.string.login_button)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
