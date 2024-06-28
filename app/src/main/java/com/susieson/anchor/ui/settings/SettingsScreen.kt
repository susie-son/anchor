package com.susieson.anchor.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.Exposures
import com.susieson.anchor.Login
import com.susieson.anchor.R
import com.susieson.anchor.model.User
import com.susieson.anchor.ui.components.AuthenticateDialog
import com.susieson.anchor.ui.components.Loading
import com.susieson.anchor.ui.components.LoginForm

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val error = viewModel.error.collectAsState().value
    val user = viewModel.user.collectAsState(null).value

    Scaffold(
        topBar = { SettingsTopBar(navController::navigateUp) },
        modifier = modifier,
    ) { innerPadding ->
        when (user) {
            null -> Loading(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding))
            else -> SettingsContent(
                user = user,
                error = error,
                navController = navController,
                onLinkAccount = viewModel::linkAccount,
                onAuthenticate = viewModel::authenticate,
                onLogout = viewModel::logout,
                onDeleteAccount = viewModel::deleteAccount,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onNavigateUp: () -> Unit, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.settings_top_bar_title)) },
        navigationIcon = {
            IconButton(onNavigateUp) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.content_description_back))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun SettingsContent(
    user: User,
    error: String?,
    onLinkAccount: (String, String) -> Unit,
    onAuthenticate: (String, String, () -> Unit) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    when {
        user.isAnonymous -> AnonymousSettings(
            error = error,
            onLinkAccount = onLinkAccount,
            modifier = modifier.padding(horizontal = 16.dp)
        )

        else -> UserSettings(
            email = user.email!!,
            error = error,
            onAuthenticate = onAuthenticate,
            onLogout = {
                onLogout()
                navigateToLogin(navController, user.id)
            },
            onDeleteAccount = {
                onDeleteAccount()
                navigateToLogin(navController, user.id)
            },
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}

private fun navigateToLogin(navController: NavController, userId: String) {
    navController.navigate(Login) {
        popUpTo(Exposures(userId)) { inclusive = true }
    }
}

@Composable
private fun AnonymousSettings(
    error: String?,
    onLinkAccount: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.anonymous_settings_body),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            LoginForm(
                email = email,
                password = password,
                isPasswordVisible = isPasswordVisible,
                errorMessage = error,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                onSubmit = { onLinkAccount(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login_create_account_button))
            }
        }
    }
}

@Preview
@Composable
private fun AnonymousSettingsPreview() {
    AnonymousSettings(
        error = "There was an error. Please try again.",
        onLinkAccount = { _, _ -> }
    )
}

@Composable
private fun UserSettings(
    email: String,
    error: String?,
    onAuthenticate: (String, String, () -> Unit) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAuthenticateDialog by remember { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf({}) }

    Column(modifier) {
        Text(
            text = stringResource(R.string.user_settings_email_label, email),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_logout_button))
        }
        FilledTonalButton(
            onClick = {
                pendingAction = onDeleteAccount
                showAuthenticateDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_delete_account_button))
        }
    }

    AuthenticateDialog(
        show = showAuthenticateDialog,
        error = error,
        email = email,
        onConfirm = { password ->
            onAuthenticate(email, password, pendingAction)
            pendingAction = {}
        },
        onShowChange = { showAuthenticateDialog = it },
    )
}

@Preview(showBackground = true)
@Composable
private fun UserSettingsPreview() {
    UserSettings(
        email = "email@example.com",
        error = "There was an error. Please try again.",
        onAuthenticate = { _, _, _ -> },
        onLogout = {},
        onDeleteAccount = {}
    )
}
