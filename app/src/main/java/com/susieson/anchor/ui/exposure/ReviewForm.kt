package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Review
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.form.DiscardConfirmationDialog
import com.susieson.anchor.ui.components.form.FormBackHandler
import com.susieson.anchor.ui.components.form.FormRatingItem
import com.susieson.anchor.ui.components.form.FormSection
import com.susieson.anchor.ui.components.form.FormSelectFilterItem
import com.susieson.anchor.ui.components.form.FormTextField
import com.susieson.anchor.ui.components.form.LabeledFormSection
import com.susieson.anchor.ui.components.form.LabeledFormTextFieldColumn
import com.susieson.anchor.ui.components.form.formTopAppBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReviewForm(
    userId: String,
    exposureId: String,
    onDiscard: () -> Unit,
    addReview: (String, String, Review) -> Unit,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier
) {
    var learnings by remember { mutableStateOf("") }
    var showDiscardConfirmation by remember { mutableStateOf(false) }

    val emotionsState = EmotionFormSectionState()
    val ratingsState = RatingFormSectionState()

    val emotionFormSectionListener = BaseEmotionFormSectionListener(emotionsState)
    val ratingFormSectionListener = BaseRatingFormSectionListener(ratingsState)

    val isValid = emotionsState.isValid && ratingsState.isValid && learnings.isNotBlank()
    val isEmpty = emotionsState.isEmpty && ratingsState.isEmpty && learnings.isBlank()

    val emotions = listOf(
        emotionsState.fear to Emotion.FEAR,
        emotionsState.sadness to Emotion.SADNESS,
        emotionsState.anxiety to Emotion.ANXIETY,
        emotionsState.guilt to Emotion.GUILT,
        emotionsState.shame to Emotion.SHAME,
        emotionsState.happiness to Emotion.HAPPINESS
    ).mapNotNull { (exists, emotion) ->
        emotion.takeIf { exists }
    }
    val review = Review(
        emotions,
        emotionsState.thoughts,
        emotionsState.sensations,
        emotionsState.behaviors,
        ratingsState.experiencingRating,
        ratingsState.anchoringRating,
        ratingsState.thinkingRating,
        ratingsState.engagingRating,
        learnings
    )

    setScaffold(
        AnchorScaffold(
            topAppBar = formTopAppBar(
                title = R.string.review_top_bar_title,
                isValid = isValid,
                isEmpty = isEmpty,
                onDiscard = onDiscard,
                onShowDiscardConfirmation = { showDiscardConfirmation = true },
                onActionClick = {
                    addReview(userId, exposureId, review)
                }
            )
        )
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        EmotionFormSection(
            emotionsState,
            emotionFormSectionListener,
            bringIntoViewRequester
        )
        RatingFormSection(ratingsState, ratingFormSectionListener)
        LearningsSection(learnings, onLearningsChange = { learnings = it }, bringIntoViewRequester)
    }
    DiscardConfirmationDialog(showDiscardConfirmation, onDiscard) { showDiscardConfirmation = it }
    FormBackHandler(isEmpty, onDiscard) { showDiscardConfirmation = true }
}

class EmotionFormSectionState {
    var fear by mutableStateOf(false)
    var sadness by mutableStateOf(false)
    var anxiety by mutableStateOf(false)
    var guilt by mutableStateOf(false)
    var shame by mutableStateOf(false)
    var happiness by mutableStateOf(false)
    val thoughts = mutableStateListOf<String>()
    val sensations = mutableStateListOf<String>()
    val behaviors = mutableStateListOf<String>()
    fun getEmotionFilters() = mapOf(
        R.string.review_fear_chip to fear,
        R.string.review_sadness_chip to sadness,
        R.string.review_anxiety_chip to anxiety,
        R.string.review_guilt_chip to guilt,
        R.string.review_shame_chip to shame,
        R.string.review_happiness_chip to happiness
    )
    val isValid = getEmotionFilters().values.contains(true) &&
        thoughts.isNotEmpty() && sensations.isNotEmpty() && behaviors.isNotEmpty()
    val isEmpty = getEmotionFilters().values.all { !it } &&
        thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty()
}

interface EmotionFormSectionListener {
    fun onEmotionFilterChanged(emotion: Int)
    fun onThoughtAdded(thought: String)
    fun onThoughtRemoved(thought: String)
    fun onSensationAdded(sensation: String)
    fun onSensationRemoved(sensation: String)
    fun onBehaviorAdded(behavior: String)
    fun onBehaviorRemoved(behavior: String)
}

class BaseEmotionFormSectionListener(private val state: EmotionFormSectionState) :
    EmotionFormSectionListener {
    override fun onEmotionFilterChanged(emotion: Int) {
        when (emotion) {
            R.string.review_fear_chip -> {
                state.fear = !state.fear
            }
            R.string.review_sadness_chip -> {
                state.sadness = !state.sadness
            }
            R.string.review_anxiety_chip -> {
                state.anxiety = !state.anxiety
            }
            R.string.review_guilt_chip -> {
                state.guilt = !state.guilt
            }
            R.string.review_shame_chip -> {
                state.shame = !state.shame
            }
            R.string.review_happiness_chip -> {
                state.happiness = !state.happiness
            }
        }
    }
    override fun onThoughtAdded(thought: String) {
        state.thoughts.add(thought)
    }
    override fun onThoughtRemoved(thought: String) {
        state.thoughts.remove(thought)
    }
    override fun onSensationAdded(sensation: String) {
        state.sensations.add(sensation)
    }
    override fun onSensationRemoved(sensation: String) {
        state.sensations.remove(sensation)
    }
    override fun onBehaviorAdded(behavior: String) {
        state.behaviors.add(behavior)
    }
    override fun onBehaviorRemoved(behavior: String) {
        state.behaviors.remove(behavior)
    }
}

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
                filters = state.getEmotionFilters(),
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

class RatingFormSectionState {
    var experiencingRating by mutableFloatStateOf(0f)
    var anchoringRating by mutableFloatStateOf(0f)
    var thinkingRating by mutableFloatStateOf(0f)
    var engagingRating by mutableFloatStateOf(0f)
    val isValid = experiencingRating > 0f && anchoringRating > 0f && thinkingRating > 0f && engagingRating > 0f
    val isEmpty = experiencingRating == 0f && anchoringRating == 0f && thinkingRating == 0f && engagingRating == 0f
}

interface RatingFormSectionListener {
    fun onExperiencingRatingChanged(rating: Float)
    fun onAnchoringRatingChanged(rating: Float)
    fun onThinkingRatingChanged(rating: Float)
    fun onEngagingRatingChanged(rating: Float)
}

class BaseRatingFormSectionListener(private val state: RatingFormSectionState) :
    RatingFormSectionListener {
    override fun onExperiencingRatingChanged(rating: Float) {
        state.experiencingRating = rating
    }
    override fun onAnchoringRatingChanged(rating: Float) {
        state.anchoringRating = rating
    }
    override fun onThinkingRatingChanged(rating: Float) {
        state.thinkingRating = rating
    }
    override fun onEngagingRatingChanged(rating: Float) {
        state.engagingRating = rating
    }
}

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LearningsSection(
    learnings: String,
    onLearningsChange: (String) -> Unit,
    bringIntoViewRequester: BringIntoViewRequester
) {
    LabeledFormSection(
        label = R.string.review_learnings_label,
        descriptionLabel = R.string.review_learnings_body,
        {
            FormTextField(
                value = learnings,
                label = null,
                errorLabel = R.string.review_learnings_error,
                isError = learnings.isEmpty(),
                imeAction = ImeAction.Done,
                onValueChange = onLearningsChange,
                singleLine = false,
                bringIntoViewRequester = bringIntoViewRequester
            )
        }
    )
}
