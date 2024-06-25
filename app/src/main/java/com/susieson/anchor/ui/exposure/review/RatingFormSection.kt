package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LabeledItem
import com.susieson.anchor.ui.components.SliderWithLabel
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState

@Composable
fun RatingFormSection(
    state: RatingFormSectionState,
    listener: RatingFormSectionListener,
    modifier: Modifier = Modifier,
) {
    LabeledItem(
        label = {
            Text(
                text = stringResource(R.string.review_effectiveness_label),
                style = MaterialTheme.typography.labelLarge
            )
        },
        content = {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LabeledItem(
                    label = {
                        Text(
                            stringResource(R.string.review_experiencing_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (state.experiencingRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    },
                    content = {
                        SliderWithLabel(
                            value = state.experiencingRating,
                            onValueChange = listener::onExperiencingRatingChanged,
                            valueRange = 0f..10f,
                            steps = 9,
                        )
                    }
                )
                LabeledItem(
                    label = {
                        Text(
                            stringResource(R.string.review_anchoring_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (state.anchoringRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    },
                    content = {
                        SliderWithLabel(
                            value = state.anchoringRating,
                            onValueChange = listener::onAnchoringRatingChanged,
                            valueRange = 0f..10f,
                            steps = 9,
                        )
                    }
                )
                LabeledItem(
                    label = {
                        Text(
                            stringResource(R.string.review_thinking_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (state.thinkingRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    },
                    content = {
                        SliderWithLabel(
                            value = state.thinkingRating,
                            onValueChange = listener::onThinkingRatingChanged,
                            valueRange = 0f..10f,
                            steps = 9,
                        )
                    }
                )
                LabeledItem(
                    label = {
                        Text(
                            stringResource(R.string.review_engaging_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (state.engagingRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    },
                    content = {
                        SliderWithLabel(
                            value = state.engagingRating,
                            onValueChange = listener::onEngagingRatingChanged,
                            valueRange = 0f..10f,
                            steps = 9,
                        )
                    }
                )
            }
        }
    )
}

data class RatingFormSectionState(
    val experiencingRating: Float = 0f,
    val anchoringRating: Float = 0f,
    val thinkingRating: Float = 0f,
    val engagingRating: Float = 0f
) : FormSectionState {
    override val isValid
        get() = experiencingRating > 0f && anchoringRating > 0f && thinkingRating > 0f && engagingRating > 0f
    override val isEmpty
        get() = experiencingRating == 0f && anchoringRating == 0f && thinkingRating == 0f && engagingRating == 0f
}

class RatingFormSectionListener(
    state: MutableState<RatingFormSectionState>
) : FormSectionListener<RatingFormSectionState>(state) {
    fun onExperiencingRatingChanged(rating: Float) {
        updateState { copy(experiencingRating = rating) }
    }
    fun onAnchoringRatingChanged(rating: Float) {
        updateState { copy(anchoringRating = rating) }
    }
    fun onThinkingRatingChanged(rating: Float) {
        updateState { copy(thinkingRating = rating) }
    }
    fun onEngagingRatingChanged(rating: Float) {
        updateState { copy(engagingRating = rating) }
    }
}
