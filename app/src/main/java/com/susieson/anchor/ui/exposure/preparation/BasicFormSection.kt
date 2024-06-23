package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.form.FormSection
import com.susieson.anchor.ui.components.form.FormTextField

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicFormSection(
    state: BasicFormSectionState,
    listener: BasicFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    FormSection(
        modifier = modifier,
        items = arrayOf(
            {
                FormTextField(
                    value = state.title,
                    label = R.string.preparation_title_label,
                    errorLabel = R.string.preparation_title_error,
                    isError = state.title.isBlank(),
                    imeAction = ImeAction.Next,
                    onValueChange = listener::onTitleChange,
                    bringIntoViewRequester = bringIntoViewRequester,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            {
                FormTextField(
                    value = state.description,
                    label = R.string.preparation_description_label,
                    errorLabel = R.string.preparation_description_error,
                    isError = state.description.isBlank(),
                    imeAction = ImeAction.Done,
                    onValueChange = listener::onDescriptionChange,
                    singleLine = false,
                    bringIntoViewRequester = bringIntoViewRequester,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    )
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
