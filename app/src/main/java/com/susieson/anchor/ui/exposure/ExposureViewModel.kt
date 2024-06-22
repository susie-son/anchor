package com.susieson.anchor.ui.exposure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import com.susieson.anchor.service.NotificationService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class ExposureViewModel
@Inject
constructor(
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : ViewModel() {

    fun getExposure(userId: String, exposureId: String): Flow<Exposure?> {
        return storageService.getExposure(userId, exposureId)
    }

    fun addPreparation(
        userId: String,
        exposureId: String,
        title: String,
        description: String,
        thoughts: List<String>,
        interpretations: List<String>,
        behaviors: List<String>,
        actions: List<String>
    ) {
        viewModelScope.launch {
            val preparation =
                Preparation(
                    thoughts = thoughts,
                    interpretations = interpretations,
                    behaviors = behaviors,
                    actions = actions
                )
            storageService.updateExposure(userId, exposureId, title, description, preparation)
        }
    }

    fun markAsInProgress(userId: String, exposureId: String, title: String) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, Status.IN_PROGRESS)
            notificationService.showReminderNotification(title, userId, exposureId)
        }
    }

    fun addReview(
        userId: String,
        exposureId: String,
        fear: Boolean,
        sadness: Boolean,
        anxiety: Boolean,
        guilt: Boolean,
        shame: Boolean,
        happiness: Boolean,
        thoughts: List<String>,
        sensations: List<String>,
        behaviors: List<String>,
        experiencingRating: Float,
        anchoringRating: Float,
        thinkingRating: Float,
        engagingRating: Float,
        learnings: String
    ) {
        viewModelScope.launch {
            val emotions =
                listOf(
                    fear to Emotion.FEAR,
                    sadness to Emotion.SADNESS,
                    anxiety to Emotion.ANXIETY,
                    guilt to Emotion.GUILT,
                    shame to Emotion.SHAME,
                    happiness to Emotion.HAPPINESS
                )
                    .filter { it.first }
                    .map { it.second }
            val review =
                Review(
                    emotions = emotions,
                    thoughts = thoughts,
                    sensations = sensations,
                    behaviors = behaviors,
                    experiencing = experiencingRating,
                    anchoring = anchoringRating,
                    thinking = thinkingRating,
                    engaging = engagingRating,
                    learnings = learnings
                )
            storageService.updateExposure(userId, exposureId, review)
        }
    }

    fun deleteExposure(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.deleteExposure(userId, exposureId)
        }
    }
}
