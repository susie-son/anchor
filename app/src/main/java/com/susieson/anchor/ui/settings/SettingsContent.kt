package com.susieson.anchor.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.model.User
import com.susieson.anchor.ui.components.AuthenticationDialog
import com.susieson.anchor.ui.components.LoginForm

@Composable
fun SettingsContent(
    user: User,
    error: String?,
    onLinkAccount: (String, String) -> Unit,
    onAuthenticate: (String, String, () -> Unit) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onNavigateLogin: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        user.isAnonymous -> AnonymousSettings(
            error = error,
            onLinkAccount = onLinkAccount,
            modifier = modifier.padding(horizontal = 16.dp)
        )

        else -> UserSettings(
            user = user,
            error = error,
            onAuthenticate = onAuthenticate,
            onLogout = {
                onLogout()
                onNavigateLogin(user.id)
            },
            onDeleteAccount = {
                onDeleteAccount()
                onNavigateLogin(user.id)
            },
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun AnonymousSettings(
    error: String?,
    onLinkAccount: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.anonymous_settings_body),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
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

@Composable
private fun UserSettings(
    user: User,
    error: String?,
    onAuthenticate: (String, String, () -> Unit) -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAuthenticateDialog by rememberSaveable { mutableStateOf(false) }
    var pendingAction by remember { mutableStateOf({}) }

    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.user_settings_email_label, user.email!!),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.settings_logout_button))
            }
            OutlinedButton(
                onClick = {
                    pendingAction = onDeleteAccount
                    showAuthenticateDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.settings_delete_account_button))
            }
        }
    }

    AuthenticationDialog(
        showDialog = showAuthenticateDialog,
        error = error,
        email = user.email!!,
        onConfirm = { password ->
            onAuthenticate(user.email, password, pendingAction)
            pendingAction = {}
        },
        onDismiss = { showAuthenticateDialog = it },
    )
}

@Preview
@Composable
private fun AnonymousSettingsPreview() {
    AnonymousSettings(
        error = "There was an error. Please try again.",
        onLinkAccount = { _, _ -> }
    )
}

@Preview
@Composable
private fun UserSettingsPreview() {
    UserSettings(
        user = User(id = "1", email = "email@example.com", isAnonymous = false),
        error = "There was an error. Please try again.",
        onAuthenticate = { _, _, _ -> },
        onLogout = {},
        onDeleteAccount = {}
    )
}
