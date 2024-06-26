package com.susieson.anchor.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LoginForm

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLogin: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val user by viewModel.user.collectAsState(null)
    val error by viewModel.error.collectAsState()
    val email by remember { viewModel.email }
    val password by remember { viewModel.password }
    val passwordVisible by remember { viewModel.passwordVisible }

    when (val userId = user?.id) {
        null -> {
            Column(
                modifier = modifier.padding(32.dp)
            ) {
                LoginForm(
                    email = email,
                    password = password,
                    passwordVisible = passwordVisible,
                    error = error,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onPasswordVisibleChange = viewModel::onPasswordVisibleChange,
                    onSubmit = { viewModel.login(email, password) },
                    submit = { Text(stringResource(R.string.login_button)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    onClick = viewModel::createAnonymousAccount,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.login_anonymous_button))
                }
            }
        }
        else -> {
            onLogin(userId)
        }
    }
}
