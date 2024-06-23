package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.form.FormTextField
import com.susieson.anchor.ui.components.form.LabeledFormSection
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LearningsFormSection(
    state: LearningsFormSectionState,
    listener: LearningsFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester
) {
    LabeledFormSection(
        label = R.string.review_learnings_label,
        descriptionLabel = R.string.review_learnings_body,
        {
            FormTextField(
                value = state.learnings,
                label = null,
                errorLabel = R.string.review_learnings_error,
                isError = state.learnings.isEmpty(),
                imeAction = ImeAction.Done,
                onValueChange = listener::onLearningsChange,
                singleLine = false,
                bringIntoViewRequester = bringIntoViewRequester
            )
        }
    )
}

data class LearningsFormSectionState(
    val learnings: String = ""
) : FormSectionState {
    override val isValid: Boolean
        get() = learnings.isNotBlank()
    override val isEmpty: Boolean
        get() = learnings.isBlank()
}

class LearningsFormSectionListener(
    state: MutableState<LearningsFormSectionState>
) : FormSectionListener<LearningsFormSectionState>(state) {
    fun onLearningsChange(learnings: String) {
        updateState { copy(learnings = learnings) }
    }
}
