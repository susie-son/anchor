package com.susieson.anchor.ui.exposure.review

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var selectedEmotions by mutableStateOf<Map<Emotion, Boolean>>(emptyMap())

    val emotions = derivedStateOf {
        Emotion.entries.associateWith { selectedEmotions[it] ?: false }
    }

    val thoughts = mutableStateListOf<String>()
    val sensations = mutableStateListOf<String>()
    val behaviors = mutableStateListOf<String>()

    var experiencing = mutableFloatStateOf(0f)
    var anchoring = mutableFloatStateOf(0f)
    var thinking = mutableFloatStateOf(0f)
    var engaging = mutableFloatStateOf(0f)

    var learnings = mutableStateOf("")

    var showDiscardDialog = mutableStateOf(false)

    val isValid = derivedStateOf {
        selectedEmotions.values.any { it } &&
            thoughts.isNotEmpty() && sensations.isNotEmpty() && behaviors.isNotEmpty() &&
            experiencing.floatValue > 0f && anchoring.floatValue > 0f &&
            thinking.floatValue > 0f && engaging.floatValue > 0f &&
            learnings.value.isNotBlank()
    }
    val isEmpty = derivedStateOf {
        emotions.value.values.all { !it } &&
            thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty() &&
            experiencing.floatValue == 0f && anchoring.floatValue == 0f &&
            thinking.floatValue == 0f && engaging.floatValue == 0f &&
            learnings.value.isBlank()
    }

    fun addReview() {
        viewModelScope.launch {
            val emotions = selectedEmotions.filterValues { it }.keys.toList()
            val review = Review(
                emotions,
                thoughts,
                sensations,
                behaviors,
                experiencing.floatValue,
                anchoring.floatValue,
                thinking.floatValue,
                engaging.floatValue,
                learnings.value
            )
            storageService.updateExposure(userId, exposure.id, review)
        }
    }

    fun onEmotionChanged(emotion: Emotion) {
        selectedEmotions = selectedEmotions.toMutableMap().apply {
            this[emotion] = !(this[emotion] ?: false)
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
        experiencing.floatValue = rating
    }

    fun onAnchoringRatingChanged(rating: Float) {
        anchoring.floatValue = rating
    }

    fun onThinkingRatingChanged(rating: Float) {
        thinking.floatValue = rating
    }

    fun onEngagingRatingChanged(rating: Float) {
        engaging.floatValue = rating
    }

    fun onLearningsChange(learnings: String) {
        this.learnings.value = learnings
    }

    fun onShowDiscardDialogChange(showDiscardDialog: Boolean) {
        this.showDiscardDialog.value = showDiscardDialog
    }
}
