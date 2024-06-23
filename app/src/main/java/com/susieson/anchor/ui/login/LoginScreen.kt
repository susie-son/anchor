package com.susieson.anchor.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.LoginForm

@Composable
fun LoginScreen(
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val error by viewModel.error.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    setScaffold(AnchorScaffold.Default)

    Column(
        modifier = modifier.padding(32.dp)
    ) {
        LoginForm(
            email = email,
            password = password,
            passwordVisible = passwordVisible,
            error = error,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onPasswordVisibleChange = { passwordVisible = !passwordVisible },
            onSubmit = { viewModel.login(email, password) },
            submitButtonText = R.string.login_button,
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
