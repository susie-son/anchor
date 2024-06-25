package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LearningsFormSection(
    state: LearningsFormSectionState,
    listener: LearningsFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    val isLearningsError = state.learnings.isEmpty()
    val coroutineScope = rememberCoroutineScope()

    LabeledItemWithSupporting(
        label = {
            Text(
                stringResource(R.string.review_learnings_label),
                style = MaterialTheme.typography.labelLarge
            )
        },
        supporting = {
            Text(
                stringResource(R.string.review_learnings_body),
                style = MaterialTheme.typography.bodySmall
            )
        },
        content = {
            OutlinedTextField(
                value = state.learnings,
                isError = isLearningsError,
                label = {},
                placeholder = {},
                supportingText = {
                    if (isLearningsError) Text(
                        stringResource(R.string.review_learnings_error),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onValueChange = listener::onLearningsChange,
                modifier = modifier
                    .padding(16.dp)
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
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
