package com.susieson.anchor.ui.exposure.review

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val emotionState = mutableStateOf(EmotionFormSectionState())
    val ratingState = mutableStateOf(RatingFormSectionState())
    val learningsState = mutableStateOf(LearningsFormSectionState())

    val emotionsListener = derivedStateOf {
        EmotionFormSectionListener(emotionState)
    }
    val ratingsListener = derivedStateOf {
        RatingFormSectionListener(ratingState)
    }
    val learningsListener = derivedStateOf {
        LearningsFormSectionListener(learningsState)
    }

    val showDiscardDialog = mutableStateOf(false)

    val isValid = derivedStateOf {
        emotionState.value.isValid && ratingState.value.isValid && learningsState.value.isValid
    }
    val isEmpty = derivedStateOf {
        emotionState.value.isEmpty && ratingState.value.isEmpty && learningsState.value.isEmpty
    }

    fun onClose() {
        if (!isEmpty.value) {
            setShowDiscardDialog(true)
        }
    }

    fun setShowDiscardDialog(show: Boolean) {
        showDiscardDialog.value = show
    }

    fun addReview() {
        viewModelScope.launch {
            val emotions = listOf(
                emotionState.value.fear to Emotion.FEAR,
                emotionState.value.sadness to Emotion.SADNESS,
                emotionState.value.anxiety to Emotion.ANXIETY,
                emotionState.value.guilt to Emotion.GUILT,
                emotionState.value.shame to Emotion.SHAME,
                emotionState.value.happiness to Emotion.HAPPINESS
            ).mapNotNull { (exists, emotion) ->
                emotion.takeIf { exists }
            }
            val review = Review(
                emotions,
                emotionState.value.thoughts,
                emotionState.value.sensations,
                emotionState.value.behaviors,
                ratingState.value.experiencingRating,
                ratingState.value.anchoringRating,
                ratingState.value.thinkingRating,
                ratingState.value.engagingRating,
                learningsState.value.learnings
            )
            storageService.updateExposure(userId, exposureId, review)
        }
    }
}
