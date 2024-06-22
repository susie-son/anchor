package com.susieson.anchor.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
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
import com.susieson.anchor.ui.AnchorScaffold
import com.susieson.anchor.ui.components.AnchorIconButton
import com.susieson.anchor.ui.components.AnchorTopAppBar
import com.susieson.anchor.ui.components.AuthenticateDialog
import com.susieson.anchor.ui.components.Loading

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val error by viewModel.error.collectAsState()
    val actionComplete = viewModel.actionComplete.collectAsState()

    var showAuthenticateDialog by remember { mutableStateOf(false) }
    var sensitiveAction by remember { mutableStateOf({}) }

    val user by viewModel.user.collectAsState(null)

    setScaffold(
        AnchorScaffold(
            topAppBar = AnchorTopAppBar(
                title = R.string.settings_top_bar_title,
                navigationIcon = AnchorIconButton(
                    onClick = onNavigateUp,
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = R.string.content_description_back
                )
            )
        )
    )

    if (user == null) {
        Loading(modifier = modifier)
        return
    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        when (user!!.isAnonymous) {
            true -> AnonymousSettings(
                onLogout = viewModel::logout,
                onDeleteAccount = viewModel::deleteAccount,
                modifier = modifier
            )

            false -> UserSettings(
                email = user!!.email!!,
                onLogout = viewModel::logout,
                onDeleteAccount = viewModel::deleteAccount,
                setSensitiveAction = { sensitiveAction = it },
                setAuthenticateDialog = { showAuthenticateDialog = it },
                modifier = modifier
            )
        }
    }

    if (actionComplete.value) {
        showAuthenticateDialog = false
        sensitiveAction = {}
        viewModel.onActionComplete()
    }

    if (showAuthenticateDialog) {
        AuthenticateDialog(
            error = error,
            onDismiss = { showAuthenticateDialog = false },
            onConfirm = { password ->
                viewModel.reauthenticate(user!!.email!!, password, sensitiveAction)
            }
        )
    }
}

@Composable
fun AnonymousSettings(
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text("Anonymous Settings")
    Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.settings_logout_button))
    }
    FilledTonalButton(
        onClick = onDeleteAccount,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.settings_delete_account_button))
    }
}

@Composable
fun UserSettings(
    email: String,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    setSensitiveAction: (() -> Unit) -> Unit,
    setAuthenticateDialog: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(stringResource(R.string.user_settings_email_label, email))
    Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.settings_logout_button))
    }
    FilledTonalButton(
        onClick = {
            setSensitiveAction(onDeleteAccount)
            setAuthenticateDialog(true)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.settings_delete_account_button))
    }
}