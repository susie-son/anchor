package com.susieson.anchor.ui.exposure.review

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Review
import com.susieson.anchor.service.StorageService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExposureReviewViewModel.Factory::class)
class ExposureReviewViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    @Assisted private val exposure: Exposure,
    private val storageService: StorageService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userId: String, exposure: Exposure): ExposureReviewViewModel
    }

    private var fear by mutableStateOf(false)
    private var sadness by mutableStateOf(false)
    private var anxiety by mutableStateOf(false)
    private var guilt by mutableStateOf(false)
    private var shame by mutableStateOf(false)
    private var happiness by mutableStateOf(false)

    val emotions = derivedStateOf {
        mapOf(
            R.string.review_fear_chip to fear,
            R.string.review_sadness_chip to sadness,
            R.string.review_anxiety_chip to anxiety,
            R.string.review_guilt_chip to guilt,
            R.string.review_shame_chip to shame,
            R.string.review_happiness_chip to happiness
        )
    }

    val thoughts = mutableStateListOf<String>()
    val sensations = mutableStateListOf<String>()
    val behaviors = mutableStateListOf<String>()

    var experiencingRating = mutableFloatStateOf(0f)
    var anchoringRating = mutableFloatStateOf(0f)
    var thinkingRating = mutableFloatStateOf(0f)
    var engagingRating = mutableFloatStateOf(0f)

    var learnings = mutableStateOf("")

    var showDiscardDialog = mutableStateOf(false)

    val isValid = derivedStateOf {
        emotions.value.values.contains(true)
                && thoughts.isNotEmpty() && sensations.isNotEmpty() && behaviors.isNotEmpty()
                && experiencingRating.floatValue > 0f && anchoringRating.floatValue > 0f
                && thinkingRating.floatValue > 0f && engagingRating.floatValue > 0f
                && learnings.value.isNotBlank()
    }
    val isEmpty = derivedStateOf {
        emotions.value.values.all { !it }
                && thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty()
                && experiencingRating.floatValue == 0f && anchoringRating.floatValue == 0f
                && thinkingRating.floatValue == 0f && engagingRating.floatValue == 0f
                && learnings.value.isBlank()
    }

    fun addReview() {
        viewModelScope.launch {
            val emotions = listOf(
                fear to Emotion.FEAR,
                sadness to Emotion.SADNESS,
                anxiety to Emotion.ANXIETY,
                guilt to Emotion.GUILT,
                shame to Emotion.SHAME,
                happiness to Emotion.HAPPINESS
            ).mapNotNull { (exists, emotion) ->
                emotion.takeIf { exists }
            }
            val review = Review(
                emotions,
                thoughts,
                sensations,
                behaviors,
                experiencingRating.floatValue,
                anchoringRating.floatValue,
                thinkingRating.floatValue,
                engagingRating.floatValue,
                learnings.value
            )
            storageService.updateExposure(userId, exposure.id, review)
        }
    }

    fun onEmotionChanged(emotion: Int) {
        when (emotion) {
            R.string.review_fear_chip -> {
                fear = !fear
            }
            R.string.review_sadness_chip -> {
                sadness = !sadness
            }
            R.string.review_anxiety_chip -> {
                anxiety = !anxiety
            }
            R.string.review_guilt_chip -> {
                guilt = !guilt
            }
            R.string.review_shame_chip -> {
                shame = !shame
            }
            R.string.review_happiness_chip -> {
                happiness = !happiness
            }
        }
    }

    fun onThoughtAdded(thought: String) {
        thoughts.add(thought)
    }

    fun onThoughtRemoved(thought: String) {
        thoughts.remove(thought)
    }

    fun onSensationAdded(sensation: String) {
        sensations.add(sensation)
    }

    fun onSensationRemoved(sensation: String) {
        sensations.remove(sensation)
    }

    fun onBehaviorAdded(behavior: String) {
        behaviors.add(behavior)
    }

    fun onBehaviorRemoved(behavior: String) {
        behaviors.remove(behavior)
    }

    fun onExperiencingRatingChanged(rating: Float) {
        experiencingRating.floatValue = rating
    }

    fun onAnchoringRatingChanged(rating: Float) {
        anchoringRating.floatValue = rating
    }

    fun onThinkingRatingChanged(rating: Float) {
        thinkingRating.floatValue = rating
    }

    fun onEngagingRatingChanged(rating: Float) {
        engagingRating.floatValue = rating
    }

    fun onLearningsChange(learnings: String) {
        this.learnings.value = learnings
    }

    fun onShowDiscardDialogChange(showDiscardDialog: Boolean) {
        this.showDiscardDialog.value = showDiscardDialog
    }
}
