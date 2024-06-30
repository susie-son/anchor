package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.susieson.anchor.R

@Composable
fun AuthenticationDialog(
    showDialog: Boolean,
    email: String,
    error: String?,
    onConfirm: (String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!showDialog) return

    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss(false) }) {
        Card(modifier) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.re_authenticate_dialog_body),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                LoginForm(
                    email = email,
                    password = password,
                    isPasswordVisible = passwordVisible,
                    errorMessage = error,
                    onEmailChange = {},
                    onPasswordChange = { password = it },
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                    onSubmit = { onConfirm(password) },
                    isEmailEnabled = false,
                    submitButtonText = { Text(stringResource(R.string.dialog_confirm)) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AuthenticateDialogPreview() {
    AuthenticationDialog(
        showDialog = true,
        email = "email@example.com",
        error = "There was an error. Please try again.",
        onDismiss = {},
        onConfirm = {}
    )
}
