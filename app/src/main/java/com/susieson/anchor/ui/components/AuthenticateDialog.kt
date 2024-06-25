package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.susieson.anchor.R

@Composable
fun AuthenticateDialog(
    show: Boolean,
    email: String,
    error: String?,
    onConfirm: (String) -> Unit,
    onShowChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!show) return

    val state = remember { mutableStateOf(LoginFormState(email = email)) }
    val form by state
    val listener = object : LoginFormListener(state) {
        override fun onSubmit() {
            onConfirm(form.password)
        }
    }

    Dialog({ onShowChange(false) }) {
        Card(modifier) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(R.string.re_authenticate_dialog_body))
                LoginForm(
                    state = form,
                    listener = listener,
                    submit = { Text(stringResource(R.string.dialog_confirm)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    LaunchedEffect(error) {
        listener.onErrorChange(error)
    }
}
