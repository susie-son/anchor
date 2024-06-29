package com.susieson.anchor.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R

@Composable
fun PasswordTextField(
    password: String,
    error: Boolean,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { LabelText(stringResource(R.string.login_password_label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        isError = error,
        supportingText = {
            ErrorText(stringResource(R.string.login_password_error, MinPasswordLength), isError = error)
        },
        trailingIcon = {
            IconButton(onPasswordVisibleChange) {
                Icon(
                    painterResource(
                        if (isPasswordVisible) {
                            R.drawable.ic_visibility
                        } else {
                            R.drawable.ic_visibility_off
                        }
                    ),
                    null
                )
            }
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PasswordTextFieldPreview() {
    PasswordTextField(
        password = "password",
        error = false,
        isPasswordVisible = false,
        onPasswordChange = {},
        onPasswordVisibleChange = {}
    )
}
