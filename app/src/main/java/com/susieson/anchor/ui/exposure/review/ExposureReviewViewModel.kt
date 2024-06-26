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
import com.susieson.anchor.model.Review
import com.susieson.anchor.service.StorageService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExposureReviewViewModel.Factory::class)
class ExposureReviewViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    @Assisted("exposureId") private val exposureId: String,
    private val storageService: StorageService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("userId") userId: String, @Assisted("exposureId") exposureId: String): ExposureReviewViewModel
    }

    private var fear by mutableStateOf(false)
    private var sadness by mutableStateOf(false)
    private var anxiety by mutableStateOf(false)
    private var guilt by mutableStateOf(false)
    private var shame by mutableStateOf(false)
    private var happiness by mutableStateOf(false)

    val emotions by derivedStateOf {
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

    var experiencingRating by mutableFloatStateOf(0f)
    var anchoringRating by mutableFloatStateOf(0f)
    var thinkingRating by mutableFloatStateOf(0f)
    var engagingRating by mutableFloatStateOf(0f)

    var learnings by mutableStateOf("")

    var showDiscardDialog by mutableStateOf(false)

    val isValid = derivedStateOf {
        emotions.values.contains(true)
                && thoughts.isNotEmpty() && sensations.isNotEmpty() && behaviors.isNotEmpty()
                && experiencingRating > 0f && anchoringRating > 0f && thinkingRating > 0f && engagingRating > 0f
                && learnings.isNotBlank()
    }
    val isEmpty by derivedStateOf {
        emotions.values.all { !it }
                && thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty()
                && experiencingRating == 0f && anchoringRating == 0f && thinkingRating == 0f && engagingRating == 0f
    }

    fun onClose() {
        if (!isEmpty) {
            showDiscardDialog = true
        }
    }

    fun onShowDiscardDialogChange(showDiscardDialog: Boolean) {
        this.showDiscardDialog = showDiscardDialog
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
                experiencingRating,
                anchoringRating,
                thinkingRating,
                engagingRating,
                learnings
            )
            storageService.updateExposure(userId, exposureId, review)
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
        experiencingRating = rating
    }

    fun onAnchoringRatingChanged(rating: Float) {
        anchoringRating = rating
    }

    fun onThinkingRatingChanged(rating: Float) {
        thinkingRating = rating
    }

    fun onEngagingRatingChanged(rating: Float) {
        engagingRating = rating
    }

    fun onLearningsChange(learnings: String) {
        this.learnings = learnings
    }
}
