package com.susieson.anchor.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import com.susieson.anchor.Exposures
import com.susieson.anchor.Login
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LoginForm

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val user by viewModel.user.collectAsState(null)
    val error by viewModel.error.collectAsState()
    val email by remember { viewModel.email }
    val password by remember { viewModel.password }
    val passwordVisible by remember { viewModel.passwordVisible }

    LaunchedEffect(user) {
        user?.let {
            navController.navigate(Exposures(it.id)) {
                popUpTo(Login) { inclusive = true }
            }
        }
    }

    Scaffold(modifier, topBar = { LoginTopBar() }) { innerPadding ->
        Column(Modifier.fillMaxWidth().padding(innerPadding)) {
            if (user == null) {
                LoginForm(
                    email = email,
                    password = password,
                    isPasswordVisible = passwordVisible,
                    errorMessage = error,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onTogglePasswordVisibility = viewModel::onPasswordVisibleChange,
                    onSubmit = { viewModel.login(email, password) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(stringResource(R.string.login_button))
                }
                OutlinedButton(
                    onClick = viewModel::createAnonymousAccount,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(stringResource(R.string.login_anonymous_button))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        modifier = modifier
    )
}
