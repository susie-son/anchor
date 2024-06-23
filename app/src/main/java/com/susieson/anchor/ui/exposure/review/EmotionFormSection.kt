package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.form.FormSection
import com.susieson.anchor.ui.components.form.FormSelectFilterItem
import com.susieson.anchor.ui.components.form.LabeledFormTextFieldColumn
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmotionFormSection(
    state: EmotionFormSectionState,
    listener: EmotionFormSectionListener,
    bringIntoViewRequester: BringIntoViewRequester
) {
    FormSection(
        {
            FormSelectFilterItem(
                label = R.string.review_emotions_label,
                filters = state.emotions,
                onFilterChange = listener::onEmotionFilterChanged
            )
        }
    )
    FormSection(
        {
            LabeledFormTextFieldColumn(
                texts = state.thoughts,
                label = R.string.review_thoughts_label,
                descriptionLabel = R.string.review_thoughts_body,
                onAdd = listener::onThoughtAdded,
                onDelete = listener::onThoughtRemoved,
                bringIntoViewRequester = bringIntoViewRequester
            )
        },
        {
            LabeledFormTextFieldColumn(
                texts = state.sensations,
                label = R.string.review_sensations_label,
                descriptionLabel = R.string.review_sensations_body,
                onAdd = listener::onSensationAdded,
                onDelete = listener::onSensationRemoved,
                bringIntoViewRequester = bringIntoViewRequester
            )
        },
        {
            LabeledFormTextFieldColumn(
                texts = state.behaviors,
                label = R.string.review_behaviors_label,
                descriptionLabel = R.string.review_behaviors_body,
                onAdd = listener::onBehaviorAdded,
                onDelete = listener::onBehaviorRemoved,
                bringIntoViewRequester = bringIntoViewRequester
            )
        }
    )
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
