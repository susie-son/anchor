package com.susieson.anchor.ui.components.form

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FormTextField(
    value: String,
    @StringRes
    label: Int?,
    @StringRes
    errorLabel: Int?,
    isError: Boolean,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = value,
        label = { label?.let { Text(stringResource(it)) } },
        placeholder = { Text(stringResource(R.string.text_field_placeholder)) },
        onValueChange = onValueChange,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        isError = isError,
        supportingText = { if (isError) errorLabel?.let { Text(stringResource(it)) } },
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            }
            .focusTarget()
    )
}
