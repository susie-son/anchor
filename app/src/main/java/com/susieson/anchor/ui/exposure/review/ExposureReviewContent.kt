package com.susieson.anchor.ui.exposure.review

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.ui.components.LabeledItemColumn
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.Operation
import com.susieson.anchor.ui.components.SliderWithLabel
import com.susieson.anchor.ui.components.TextFieldColumn

@Suppress("LongParameterList")
@Composable
fun ExposureReviewContent(
    emotions: Map<Emotion, Boolean>,
    thoughts: List<String>,
    sensations: List<String>,
    behaviors: List<String>,
    experiencingRating: Float,
    anchoringRating: Float,
    thinkingRating: Float,
    engagingRating: Float,
    learnings: String,
    onEmotionChange: (Emotion) -> Unit,
    onThoughtChange: (Operation, String) -> Unit,
    onSensationChange: (Operation, String) -> Unit,
    onBehaviorChange: (Operation, String) -> Unit,
    onExperiencingRatingChange: (Float) -> Unit,
    onAnchoringRatingChange: (Float) -> Unit,
    onThinkingRatingChange: (Float) -> Unit,
    onEngagingRatingChange: (Float) -> Unit,
    onLearningsChange: (String) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EmotionsSection(
            emotions = emotions,
            thoughts = thoughts,
            sensations = sensations,
            behaviors = behaviors,
            onEmotionChange = onEmotionChange,
            onThoughtChange = onThoughtChange,
            onSensationChange = onSensationChange,
            onBehaviorChange = onBehaviorChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        RatingsSection(
            experiencingRating = experiencingRating,
            anchoringRating = anchoringRating,
            thinkingRating = thinkingRating,
            engagingRating = engagingRating,
            onExperiencingRatingChange = onExperiencingRatingChange,
            onAnchoringRatingChange = onAnchoringRatingChange,
            onThinkingRatingChange = onThinkingRatingChange,
            onEngagingRatingChange = onEngagingRatingChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LearningsSection(
            learnings = learnings,
            onLearningsChange = onLearningsChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmotionsSection(
    emotions: Map<Emotion, Boolean>,
    thoughts: List<String>,
    sensations: List<String>,
    behaviors: List<String>,
    onEmotionChange: (Emotion) -> Unit,
    onThoughtChange: (Operation, String) -> Unit,
    onSensationChange: (Operation, String) -> Unit,
    onBehaviorChange: (Operation, String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class EmotionCategory(
        @StringRes val label: Int,
        @StringRes val body: Int,
        val items: List<String>,
        val onChange: (Operation, String) -> Unit
    )
    val items = listOf(
        EmotionCategory(
            R.string.review_thoughts_label,
            R.string.review_thoughts_body,
            thoughts,
            onThoughtChange
        ),
        EmotionCategory(
            R.string.review_sensations_label,
            R.string.review_sensations_body,
            sensations,
            onSensationChange
        ),
        EmotionCategory(
            R.string.review_behaviors_label,
            R.string.review_behaviors_body,
            behaviors,
            onBehaviorChange
        )
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledItemColumn(
            labelText = {
                Text(
                    text = stringResource(R.string.review_emotions_label),
                    color = if (emotions.all { !it.value }) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                emotions.forEach { (emotion, selected) ->
                    FilterChip(
                        selected = selected,
                        onClick = { onEmotionChange(emotion) },
                        label = { Text(stringResource(emotion.label)) }
                    )
                }
            }
        }
        items.forEach { category ->
            LabeledItemWithSupporting(
                labelText = { Text(stringResource(category.label)) },
                supportingText = {
                    Text(
                        text = stringResource(category.body),
                        color = if (category.items.isEmpty()) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            ) {
                TextFieldColumn(
                    texts = category.items,
                    onAdd = { category.onChange(Operation.ADD, it) },
                    onRemove = { category.onChange(Operation.REMOVE, it) }
                )
            }
        }
    }
}

@Composable
private fun RatingsSection(
    experiencingRating: Float,
    anchoringRating: Float,
    thinkingRating: Float,
    engagingRating: Float,
    onExperiencingRatingChange: (Float) -> Unit,
    onAnchoringRatingChange: (Float) -> Unit,
    onThinkingRatingChange: (Float) -> Unit,
    onEngagingRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    data class RatingCategory(
        @StringRes
        val label: Int,
        val rating: Float,
        val onRatingChange: (Float) -> Unit
    )
    val items = listOf(
        RatingCategory(R.string.review_experiencing_label, experiencingRating, onExperiencingRatingChange),
        RatingCategory(R.string.review_anchoring_label, anchoringRating, onAnchoringRatingChange),
        RatingCategory(R.string.review_thinking_label, thinkingRating, onThinkingRatingChange),
        RatingCategory(R.string.review_engaging_label, engagingRating, onEngagingRatingChange)
    )
    Column(modifier) {
        LabeledItemColumn({ Text(stringResource(R.string.review_effectiveness_label)) }) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items.forEach { category ->
                    LabeledItemColumn({
                        Text(
                            text = stringResource(category.label),
                            color = if (category.rating == 0f) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }) {
                        SliderWithLabel(
                            value = category.rating,
                            onValueChange = category.onRatingChange,
                            valueRange = 0f..10f,
                            steps = 9,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearningsSection(
    learnings: String,
    onLearningsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LabeledItemWithSupporting(
        labelText = { Text(stringResource(R.string.review_learnings_label)) },
        supportingText = { Text(stringResource(R.string.review_learnings_body)) },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = learnings,
            isError = learnings.isBlank(),
            supportingText = {
                if (learnings.isBlank()) {
                    Text(stringResource(R.string.review_learnings_error))
                }
            },
            onValueChange = onLearningsChange,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExposureReviewContentPreview() {
    ExposureReviewContent(
        emotions = mapOf(
            Emotion.FEAR to true,
            Emotion.GUILT to false,
            Emotion.SHAME to true,
            Emotion.ANXIETY to false,
            Emotion.SADNESS to true,
            Emotion.ANGER to false,
            Emotion.HAPPINESS to true
        ),
        thoughts = listOf("thought 1", "thought 2", "thought 3"),
        sensations = listOf("sensation 1", "sensation 2", "sensation 3"),
        behaviors = listOf("behavior 1", "behavior 2", "behavior 3"),
        experiencingRating = 0f,
        anchoringRating = 2f,
        thinkingRating = 5f,
        engagingRating = 10f,
        learnings = "Learnings",
        onEmotionChange = {},
        onThoughtChange = { _, _ -> },
        onSensationChange = { _, _ -> },
        onBehaviorChange = { _, _ -> },
        onExperiencingRatingChange = {},
        onAnchoringRatingChange = {},
        onThinkingRatingChange = {},
        onEngagingRatingChange = {},
        onLearningsChange = {},
        innerPadding = PaddingValues()
    )
}
