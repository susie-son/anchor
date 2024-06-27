package com.susieson.anchor.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.susieson.anchor.R

@Composable
fun EmailTextField(
    email: String,
    readOnly: Boolean,
    isError: Boolean,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text(stringResource(R.string.login_email_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ).merge(keyboardOptions),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(stringResource(R.string.login_email_error))
            }
        },
        readOnly = readOnly,
        modifier = modifier
    )
}
