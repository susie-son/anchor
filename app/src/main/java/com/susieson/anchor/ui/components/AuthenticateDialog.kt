package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.susieson.anchor.R

@Composable
fun AuthenticateDialog(
    email: String,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(R.string.re_authenticate_dialog_body))
                LoginForm(
                    email = email,
                    password = password,
                    passwordVisible = passwordVisible,
                    error = error,
                    emailEnabled = false,
                    onEmailChange = {},
                    onPasswordChange = { password = it },
                    onPasswordVisibleChange = { passwordVisible = !passwordVisible },
                    onSubmit = { onConfirm(password) },
                    submitButtonText = R.string.dialog_confirm,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
