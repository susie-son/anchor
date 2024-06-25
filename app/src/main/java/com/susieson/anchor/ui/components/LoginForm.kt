package com.susieson.anchor.ui.components

import android.annotation.SuppressLint
import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState
import com.susieson.anchor.ui.theme.AnchorTheme

const val MinPasswordLength = 6

@Composable
fun LoginForm(
    state: LoginFormState,
    listener: LoginFormListener,
    submit: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isEmailEnabled: Boolean = true
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
            isError = emailError,
            enabled = isEmailEnabled,
            onEmailChange = listener::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        PasswordTextField(
            password = password,
            error = passwordError,
            isPasswordVisible = passwordVisible,
            onPasswordChange = listener::onPasswordChange,
            onPasswordVisibleChange = listener::onPasswordVisibleChange,
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
            onClick = listener::onSubmit,
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            submit()
        }
    }
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val error: String? = null
) : FormSectionState {
    override val isValid: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() &&
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length >= MinPasswordLength
    override val isEmpty: Boolean
        get() = email.isEmpty() && password.isEmpty()
}

abstract class LoginFormListener(
    state: MutableState<LoginFormState>
) : FormSectionListener<LoginFormState>(state) {
    abstract fun onSubmit()
    fun onEmailChange(email: String) {
        updateState { copy(email = email) }
    }
    fun onPasswordChange(password: String) {
        updateState { copy(password = password) }
    }
    fun onPasswordVisibleChange() {
        updateState { copy(passwordVisible = !passwordVisible) }
    }
    fun onErrorChange(error: String?) {
        updateState { copy(error = error) }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    val state = LoginFormState(
        email = "email@example.com",
        password = "123",
        passwordVisible = false,
        error = "There was an error logging in. Please try again."
    )
    val listener = @SuppressLint("UnrememberedMutableState")
    object : LoginFormListener(mutableStateOf(state)) {
        override fun onSubmit() {}
    }
    AnchorTheme {
        LoginForm(
            state = state,
            listener = listener,
            submit = { Text(stringResource(R.string.login_button)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
