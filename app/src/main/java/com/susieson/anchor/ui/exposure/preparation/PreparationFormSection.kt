package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.form.FormSection
import com.susieson.anchor.ui.components.form.LabeledFormTextFieldColumn
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreparationFormSection(
    state: PreparationFormSectionState,
    listener: PreparationFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    FormSection(
        modifier = modifier,
        items = arrayOf(
            {
                LabeledFormTextFieldColumn(
                    texts = state.thoughts,
                    label = R.string.preparation_thoughts_label,
                    descriptionLabel = R.string.preparation_thoughts_body,
                    onAdd = listener::onThoughtAdded,
                    onDelete = listener::onThoughtRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
                LabeledFormTextFieldColumn(
                    texts = state.interpretations,
                    label = R.string.preparation_interpretations_label,
                    descriptionLabel = R.string.preparation_interpretations_body,
                    onAdd = listener::onInterpretationAdded,
                    onDelete = listener::onInterpretationRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = state.behaviors,
                    label = R.string.preparation_behaviors_label,
                    descriptionLabel = R.string.preparation_behaviors_body,
                    onAdd = listener::onBehaviorAdded,
                    onDelete = listener::onBehaviorRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = state.actions,
                    label = R.string.preparation_actions_label,
                    descriptionLabel = R.string.preparation_actions_body,
                    onAdd = listener::onActionAdded,
                    onDelete = listener::onActionRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            }
        )
    )
}

data class PreparationFormSectionState(
    val thoughts: List<String> = listOf(),
    val interpretations: List<String> = listOf(),
    val behaviors: List<String> = listOf(),
    val actions: List<String> = listOf(),
) : FormSectionState {
    override val isValid: Boolean
        get() = thoughts.isNotEmpty() && interpretations.isNotEmpty() &&
            behaviors.isNotEmpty() && actions.isNotEmpty()
    override val isEmpty: Boolean
        get() = thoughts.isEmpty() && interpretations.isEmpty() &&
            behaviors.isEmpty() && actions.isEmpty()
}

abstract class PreparationFormSectionListener(
    state: MutableState<PreparationFormSectionState>
) : FormSectionListener<PreparationFormSectionState>(state) {
    abstract fun onThoughtAdded(thought: String)
    abstract fun onThoughtRemoved(thought: String)
    abstract fun onInterpretationAdded(interpretation: String)
    abstract fun onInterpretationRemoved(interpretation: String)
    abstract fun onBehaviorAdded(behavior: String)
    abstract fun onBehaviorRemoved(behavior: String)
    abstract fun onActionAdded(action: String)
    abstract fun onActionRemoved(action: String)
}

class BasePreparationFormSectionListener(
    state: MutableState<PreparationFormSectionState>
) : PreparationFormSectionListener(state) {
    override fun onThoughtAdded(thought: String) {
        updateState { copy(thoughts = state.value.thoughts + thought) }
    }
    override fun onThoughtRemoved(thought: String) {
        updateState { copy(thoughts = state.value.thoughts - thought) }
    }
    override fun onInterpretationAdded(interpretation: String) {
        updateState { copy(interpretations = state.value.interpretations + interpretation) }
    }
    override fun onInterpretationRemoved(interpretation: String) {
        updateState { copy(interpretations = state.value.interpretations - interpretation) }
    }
    override fun onBehaviorAdded(behavior: String) {
        updateState { copy(behaviors = state.value.behaviors + behavior) }
    }
    override fun onBehaviorRemoved(behavior: String) {
        updateState { copy(behaviors = state.value.behaviors - behavior) }
    }
    override fun onActionAdded(action: String) {
        updateState { copy(actions = state.value.actions + action) }
    }
    override fun onActionRemoved(action: String) {
        updateState { copy(actions = state.value.actions - action) }
    }
}
