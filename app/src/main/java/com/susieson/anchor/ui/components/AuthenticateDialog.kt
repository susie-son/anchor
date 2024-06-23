package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.form.BaseLoginFormListener
import com.susieson.anchor.ui.components.form.LoginForm
import com.susieson.anchor.ui.components.form.LoginFormState

@Composable
fun AuthenticateDialog(
    email: String,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = LoginFormState(
        email = email,
        password = "",
        passwordVisible = false,
        error = error
    )
    val listener = object : BaseLoginFormListener(state) {
        override fun onSubmit() {
            onConfirm(state.password)
        }
    }

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
                    state = state,
                    listener = listener,
                    submitButtonText = R.string.dialog_confirm,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
