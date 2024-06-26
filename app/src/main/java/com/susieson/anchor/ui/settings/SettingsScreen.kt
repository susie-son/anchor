package com.susieson.anchor.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.AuthenticateDialog
import com.susieson.anchor.ui.components.Loading
import com.susieson.anchor.ui.components.LoginForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val error by viewModel.error.collectAsState()
    val userState by viewModel.user.collectAsState(null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings_top_bar_title)) },
                navigationIcon = {
                    IconButton(navController::navigateUp) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.content_description_back)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (val user = userState) {
                null -> Loading(modifier = modifier.fillMaxSize())
                else -> {
                    when (user.isAnonymous) {
                        true -> AnonymousSettings(
                            error = error,
                            onLinkAccount = { email, password ->
                                viewModel.linkAccount(email, password)
                                navController.navigateUp()
                            },
                            onDeleteAccount = viewModel::deleteAccount,
                            modifier = modifier
                        )
                        false -> UserSettings(
                            email = user.email!!,
                            error = error,
                            onAuthenticate = { email: String, password: String, action: () -> Unit ->
                                viewModel.reAuthenticate(email, password, action)
                            },
                            onLogout = viewModel::logout,
                            onDeleteAccount = viewModel::deleteAccount,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnonymousSettings(
    error: String?,
    onLinkAccount: (String, String) -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(R.string.anonymous_settings_body))
                LoginForm(
                    email = email,
                    password = password,
                    passwordVisible = passwordVisible,
                    error = error,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onPasswordVisibleChange = { passwordVisible = !passwordVisible },
                    onSubmit = { onLinkAccount(email, password) },
                    submit = { Text(stringResource(R.string.login_create_account_button)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = onDeleteAccount,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_delete_account_button))
        }
    }
}

@Composable
fun UserSettings(
    email: String,
    error: String?,
    onAuthenticate: (String, String, () -> Unit) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAuthenticateDialog by remember { mutableStateOf(false) }
    var sensitiveAction by remember { mutableStateOf({}) }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.user_settings_email_label, email))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_logout_button))
        }
        FilledTonalButton(
            onClick = {
                sensitiveAction = onDeleteAccount
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
            onAuthenticate(email, password, sensitiveAction)
        },
        onShowChange = { showAuthenticateDialog = it },
    )
}
