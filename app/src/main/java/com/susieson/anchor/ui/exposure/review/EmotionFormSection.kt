package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LabeledItem
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.TextFieldColumn
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun EmotionFormSection(
    state: EmotionFormSectionState,
    listener: EmotionFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LabeledItem(
            label = {
                Text(
                    stringResource(R.string.review_emotions_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            content = {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.emotions.forEach { (emotion, selected) ->
                        FilterChip(
                            selected = selected,
                            onClick = { listener.onEmotionFilterChanged(emotion) },
                            label = { Text(stringResource(emotion)) }
                        )
                    }
                }
            }
        )
    }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.review_thoughts_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.review_thoughts_body),
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
                    stringResource(R.string.review_sensations_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.review_sensations_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (state.sensations.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                )
            },
            content = {
                TextFieldColumn(
                    texts = state.sensations,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = listener::onSensationAdded,
                    onDelete = listener::onSensationRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.review_behaviors_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.review_behaviors_body),
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
    }
}

data class EmotionFormSectionState(
    val fear: Boolean = false,
    val sadness: Boolean = false,
    val anxiety: Boolean = false,
    val guilt: Boolean = false,
    val shame: Boolean = false,
    val happiness: Boolean = false,
    val thoughts: List<String> = listOf(),
    val sensations: List<String> = listOf(),
    val behaviors: List<String> = listOf(),
) : FormSectionState {
    override val isValid: Boolean
        get() = emotions.values.contains(true) &&
            thoughts.isNotEmpty() && sensations.isNotEmpty() && behaviors.isNotEmpty()
    override val isEmpty: Boolean
        get() = emotions.values.all { !it } &&
            thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty()
    val emotions = mapOf(
        R.string.review_fear_chip to fear,
        R.string.review_sadness_chip to sadness,
        R.string.review_anxiety_chip to anxiety,
        R.string.review_guilt_chip to guilt,
        R.string.review_shame_chip to shame,
        R.string.review_happiness_chip to happiness
    )
}

class EmotionFormSectionListener(
    state: MutableState<EmotionFormSectionState>
) : FormSectionListener<EmotionFormSectionState>(state) {
    fun onEmotionFilterChanged(emotion: Int) {
        when (emotion) {
            R.string.review_fear_chip -> {
                updateState { copy(fear = !fear) }
            }
            R.string.review_sadness_chip -> {
                updateState { copy(sadness = !sadness) }
            }
            R.string.review_anxiety_chip -> {
                updateState { copy(anxiety = !anxiety) }
            }
            R.string.review_guilt_chip -> {
                updateState { copy(guilt = !guilt) }
            }
            R.string.review_shame_chip -> {
                updateState { copy(shame = !shame) }
            }
            R.string.review_happiness_chip -> {
                updateState { copy(happiness = !happiness) }
            }
        }
    }
    fun onThoughtAdded(thought: String) {
        updateState { copy(thoughts = thoughts + thought) }
    }
    fun onThoughtRemoved(thought: String) {
        updateState { copy(thoughts = thoughts - thought) }
    }
    fun onSensationAdded(sensation: String) {
        updateState { copy(sensations = sensations + sensation) }
    }
    fun onSensationRemoved(sensation: String) {
        updateState { copy(sensations = sensations - sensation) }
    }
    fun onBehaviorAdded(behavior: String) {
        updateState { copy(behaviors = behaviors + behavior) }
    }
    fun onBehaviorRemoved(behavior: String) {
        updateState { copy(behaviors = behaviors - behavior) }
    }
}
