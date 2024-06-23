package com.susieson.anchor.ui.exposure.review

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.form.FormRatingItem
import com.susieson.anchor.ui.components.form.LabeledFormSection
import com.susieson.anchor.ui.exposure.FormSectionListener
import com.susieson.anchor.ui.exposure.FormSectionState

@Composable
fun RatingFormSection(
    state: RatingFormSectionState,
    listener: RatingFormSectionListener
) {
    LabeledFormSection(
        label = R.string.review_effectiveness_label,
        descriptionLabel = null,
        {
            FormRatingItem(
                label = R.string.review_experiencing_label,
                value = state.experiencingRating,
                onValueChange = listener::onExperiencingRatingChanged,
            )
        },
        {
            FormRatingItem(
                label = R.string.review_anchoring_label,
                value = state.anchoringRating,
                onValueChange = listener::onAnchoringRatingChanged,
            )
        },
        {
            FormRatingItem(
                label = R.string.review_thinking_label,
                value = state.thinkingRating,
                onValueChange = listener::onThinkingRatingChanged,
            )
        },
        {
            FormRatingItem(
                label = R.string.review_engaging_label,
                value = state.engagingRating,
                onValueChange = listener::onEngagingRatingChanged,
            )
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
