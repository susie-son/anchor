package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicFormSection(
    state: BasicFormSectionState,
    listener: BasicFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    val isTitleError = state.title.isBlank()
    val isDescriptionError = state.description.isBlank()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.title,
            isError = isTitleError,
            label = { Text(stringResource(R.string.preparation_title_label)) },
            placeholder = {},
            supportingText = {
                if (isTitleError) Text(
                    stringResource(R.string.preparation_title_error),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = listener::onTitleChange,
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = state.description,
            isError = isDescriptionError,
            label = { Text(stringResource(R.string.preparation_description_label)) },
            placeholder = {},
            supportingText = {
                if (isDescriptionError) Text(
                    stringResource(R.string.preparation_description_error),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = listener::onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
    }
}

data class BasicFormSectionState(
    val title: String = "",
    val description: String = ""
) {
    val isValid: Boolean
        get() = title.isNotBlank() && description.isNotBlank()
    val isEmpty: Boolean
        get() = title.isBlank() && description.isBlank()
}

interface BasicFormSectionListener {
    fun onTitleChange(title: String)
    fun onDescriptionChange(description: String)
}

class BaseBasicFormSectionListener(
    val state: MutableState<BasicFormSectionState>
) : BasicFormSectionListener {
    override fun onTitleChange(title: String) {
        state.value = state.value.copy(title = title)
    }
    override fun onDescriptionChange(description: String) {
        state.value = state.value.copy(description = description)
    }
}
