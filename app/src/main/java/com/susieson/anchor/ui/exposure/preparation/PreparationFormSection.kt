package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.TextFieldColumn
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
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_thoughts_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_thoughts_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.thoughts.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                )
            },
            content = {
                TextFieldColumn(
                    texts = state.thoughts,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = listener::onThoughtAdded,
                    onDelete = listener::onThoughtRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_interpretations_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_interpretations_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.interpretations.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                )
            },
            content = {
                TextFieldColumn(
                    texts = state.interpretations,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = listener::onInterpretationAdded,
                    onDelete = listener::onInterpretationRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_behaviors_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_behaviors_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.behaviors.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                )
            },
            content = {
                TextFieldColumn(
                    texts = state.behaviors,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = listener::onBehaviorAdded,
                    onDelete = listener::onBehaviorRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_actions_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_actions_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.actions.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                )
            },
            content = {
                TextFieldColumn(
                    texts = state.actions,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = listener::onActionAdded,
                    onDelete = listener::onActionRemoved
                )
            }
        )
    }
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
