package com.susieson.anchor.ui.components.form

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.susieson.anchor.R

@Composable
fun EmailTextField(
    email: String,
    emailEnabled: Boolean,
    emailError: Boolean,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text(stringResource(R.string.login_email_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = emailError,
        supportingText = {
            if (emailError) {
                Text(stringResource(R.string.login_email_error))
            }
        },
        enabled = emailEnabled,
        modifier = modifier
    )
}
