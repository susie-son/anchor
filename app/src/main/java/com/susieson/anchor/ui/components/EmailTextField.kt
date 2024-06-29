package com.susieson.anchor.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R

@Composable
fun EmailTextField(
    email: String,
    enabled: Boolean,
    isError: Boolean,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { LabelText(stringResource(R.string.login_email_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ).merge(keyboardOptions),
        isError = isError,
        supportingText = {
            if (isError) {
                BodyText(stringResource(R.string.login_email_error))
            }
        },
        enabled = enabled,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun EmailTextFieldPreview() {
    EmailTextField(
        email = "email@example.com",
        enabled = true,
        isError = false,
        onEmailChange = {}
    )
}
