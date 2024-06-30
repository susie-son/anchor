package com.susieson.anchor.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LoginForm

@Composable
fun LoginScreen(
    onNavigateExposures: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsState(null)
    val error by viewModel.error.collectAsState(null)
    val state by viewModel.state.collectAsState()

    val navigate = remember { onNavigateExposures }

    LaunchedEffect(user) {
        user?.let { navigate(it.id) }
    }

    Scaffold(modifier, topBar = { LoginTopBar() }) { innerPadding ->
        Column(Modifier.fillMaxWidth().padding(innerPadding)) {
            if (user == null) {
                LoginForm(
                    email = state.email,
                    password = state.password,
                    isPasswordVisible = state.passwordVisible,
                    errorMessage = error,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onTogglePasswordVisibility = { viewModel.onPasswordVisibleChange(!state.passwordVisible) },
                    onSubmit = viewModel::login,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(stringResource(R.string.login_button))
                }
                OutlinedButton(
                    onClick = viewModel::createAnonymousAccount,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(stringResource(R.string.login_anonymous_button))
                }
            }
        }
    }
}
