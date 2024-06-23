package com.susieson.anchor.ui.components

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.theme.AnchorTheme

const val MinPasswordLength = 6

@Composable
fun LoginForm(
    state: LoginFormState,
    listener: LoginFormListener,
    @StringRes
    submitButtonText: Int,
    modifier: Modifier = Modifier,
    emailEnabled: Boolean = true
) {
    val email = state.email
    val password = state.password
    val passwordVisible = state.passwordVisible
    val error = state.error

    val emailError = email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val passwordError = password.isNotEmpty() && password.length < MinPasswordLength

    val isValid = email.isNotEmpty() && password.isNotEmpty() && !emailError && !passwordError

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        EmailTextField(
            email = email,
            emailError = emailError,
            emailEnabled = emailEnabled,
            onEmailChange = listener::onEmailChange,
        )
        PasswordTextField(
            password = password,
            passwordError = passwordError,
            passwordVisible = passwordVisible,
            onPasswordChange = listener::onPasswordChange,
            onPasswordVisibleChange = listener::onPasswordVisibleChange,
        )
        error?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Button(
            onClick = listener::onSubmit,
            enabled = isValid,
        ) {
            Text(stringResource(submitButtonText))
        }
    }
}

class LoginFormState(email: String, password: String, passwordVisible: Boolean, error: String?) {
    var email by mutableStateOf(email)
    var password by mutableStateOf(password)
    var passwordVisible by mutableStateOf(passwordVisible)
    var error by mutableStateOf(error)
}

interface LoginFormListener {
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onPasswordVisibleChange()
    fun onSubmit()
}

abstract class BaseLoginFormListener(private val state: LoginFormState) : LoginFormListener {
    override fun onEmailChange(email: String) {
        state.email = email
    }
    override fun onPasswordChange(password: String) {
        state.password = password
    }
    override fun onPasswordVisibleChange() {
        state.passwordVisible = !state.passwordVisible
    }
}

@Composable
fun EmailTextField(
    email: String,
    emailEnabled: Boolean,
    emailError: Boolean,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text(stringResource(R.string.login_email_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = emailError,
        supportingText = {
            if (emailError) {
                Text(stringResource(R.string.login_email_error))
            }
        },
        enabled = emailEnabled,
        modifier = modifier
    )
}

@Composable
fun PasswordTextField(
    password: String,
    passwordError: Boolean,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(stringResource(R.string.login_password_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        isError = passwordError,
        supportingText = {
            if (passwordError) {
                Text(stringResource(R.string.login_password_error, MinPasswordLength))
            }
        },
        trailingIcon = {
            IconButton(onClick = onPasswordVisibleChange) {
                Icon(
                    painterResource(
                        if (passwordVisible) {
                            R.drawable.ic_visibility
                        } else {
                            R.drawable.ic_visibility_off
                        }
                    ),
                    null
                )
            }
        },
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    AnchorTheme {
        LoginForm(
            state = LoginFormState(
                email = "email@example.com",
                password = "123",
                passwordVisible = false,
                error = "There was an error logging in. Please try again."
            ),
            listener = object : LoginFormListener {
                override fun onEmailChange(email: String) {}
                override fun onPasswordChange(password: String) {}
                override fun onPasswordVisibleChange() {}
                override fun onSubmit() {}
            },
            submitButtonText = R.string.login_button,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
