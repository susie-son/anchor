package com.susieson.anchor.ui.exposure.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Review
import com.susieson.anchor.service.StorageService
import com.susieson.anchor.ui.components.Operation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _state = MutableStateFlow(ExposureReviewState())
    val state: StateFlow<ExposureReviewState> = _state.asStateFlow()

    fun addReview() {
        viewModelScope.launch {
            with(state.value) {
                val review = Review(
                    emotionsList,
                    thoughts,
                    sensations,
                    behaviors,
                    experiencing,
                    anchoring,
                    thinking,
                    engaging,
                    learnings
                )
                storageService.updateExposure(userId, exposure.id, review)
            }
        }
    }

    fun onEmotionChanged(emotion: Emotion) {
        updateField({ copy(emotions = emotions + (emotion to !(emotions[emotion] ?: false))) }, emotion)
    }

    fun onThoughtChanged(operation: Operation, thought: String) {
        updateField({ copy(thoughts = updateList(thoughts, thought, operation)) }, thought)
    }

    fun onSensationChanged(operation: Operation, sensation: String) {
        updateField({ copy(sensations = updateList(sensations, sensation, operation)) }, sensation)
    }

    fun onBehaviorChanged(operation: Operation, behavior: String) {
        updateField({ copy(behaviors = updateList(behaviors, behavior, operation)) }, behavior)
    }

    fun onExperiencingRatingChanged(rating: Float) {
        updateField({ copy(experiencing = rating) }, rating)
    }

    fun onAnchoringRatingChanged(rating: Float) {
        updateField({ copy(anchoring = rating) }, rating)
    }

    fun onThinkingRatingChanged(rating: Float) {
        updateField({ copy(thinking = rating) }, rating)
    }

    fun onEngagingRatingChanged(rating: Float) {
        updateField({ copy(engaging = rating) }, rating)
    }

    fun onLearningsChanged(learnings: String) {
        updateField({ copy(learnings = learnings) }, learnings)
    }

    fun onShowDiscardDialogChanged(showDiscardDialog: Boolean) {
        updateField({ copy(showDiscardDialog = showDiscardDialog) }, showDiscardDialog)
    }

    private fun <T> updateField(updater: ExposureReviewState.(T) -> ExposureReviewState, value: T) {
        _state.value = state.value.updater(value)
    }

    private fun <T> updateList(list: List<T>, item: T, operation: Operation): List<T> {
        return when (operation) {
            Operation.ADD -> listOf(item) + list
            Operation.REMOVE -> list - item
        }
    }
}

data class ExposureReviewState(
    val emotions: Map<Emotion, Boolean> = mapOf(
        Emotion.FEAR to false,
        Emotion.SADNESS to false,
        Emotion.ANXIETY to false,
        Emotion.GUILT to false,
        Emotion.SHAME to false,
        Emotion.ANGER to false,
        Emotion.HAPPINESS to false
    ),
    val thoughts: List<String> = emptyList(),
    val sensations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val experiencing: Float = 0f,
    val anchoring: Float = 0f,
    val thinking: Float = 0f,
    val engaging: Float = 0f,
    val learnings: String = "",
    val showDiscardDialog: Boolean = false
) {
    val isEmpty = emotions.all { !it.value } && thoughts.isEmpty() && sensations.isEmpty() &&
        behaviors.isEmpty() && experiencing == 0f && anchoring == 0f && thinking == 0f && engaging == 0f &&
        learnings.isBlank()
    val isValid = emotions.any { it.value } && thoughts.isNotEmpty() && sensations.isNotEmpty() &&
        behaviors.isNotEmpty() && experiencing > 0f && anchoring > 0f && thinking > 0f && engaging > 0f &&
        learnings.isNotBlank()
    val emotionsList = emotions.filterValues { it }.keys.toList()
}
