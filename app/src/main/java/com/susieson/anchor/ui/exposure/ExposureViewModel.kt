package com.susieson.anchor.ui.exposure

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.service.NotificationService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposureViewModel @Inject constructor(
    private val authService: AuthService,
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : ViewModel() {

    private lateinit var userId: String
    private lateinit var exposureId: String

    init {
        viewModelScope.launch {
            userId = authService.getUserId()
        }
    }

    fun getExposure(exposureId: String) {
        this.exposureId = exposureId
        viewModelScope.launch {
            storageService.getExposure(userId, exposureId).collect {
                exposure.value = it
            }
        }
    }

    val exposure = mutableStateOf<Exposure?>(null)

    val title = mutableStateOf("")
    val description = mutableStateOf("")

    val preparationThoughts = mutableStateListOf<String>()
    val preparationInterpretations = mutableStateListOf<String>()
    val preparationBehaviors = mutableStateListOf<String>()
    val preparationActions = mutableStateListOf<String>()

    val fear = mutableStateOf(false)
    var sadness = mutableStateOf(false)
    var anxiety = mutableStateOf(false)
    var guilt = mutableStateOf(false)
    var shame = mutableStateOf(false)
    var happiness = mutableStateOf(false)

    val reviewThoughts = mutableStateListOf<String>()
    val reviewSensations = mutableStateListOf<String>()
    val reviewBehaviors = mutableStateListOf<String>()

    val experiencingRating = mutableFloatStateOf(0f)
    val anchoringRating = mutableFloatStateOf(0f)
    val thinkingRating = mutableFloatStateOf(0f)
    val engagingRating = mutableFloatStateOf(0f)

    val learnings = mutableStateOf("")

    fun setTitle(title: String) {
        this.title.value = title
    }

    fun setDescription(description: String) {
        this.description.value = description
    }

    fun addPreparationThought(thought: String) {
        preparationThoughts.add(thought)
    }

    fun addPreparationInterpretation(interpretation: String) {
        preparationInterpretations.add(interpretation)
    }

    fun addPreparationBehavior(behavior: String) {
        preparationBehaviors.add(behavior)
    }

    fun addPreparationAction(action: String) {
        preparationActions.add(action)
    }

    fun removePreparationThought(thought: String) {
        preparationThoughts.remove(thought)
    }

    fun removePreparationInterpretation(interpretation: String) {
        preparationInterpretations.remove(interpretation)
    }

    fun removePreparationBehavior(behavior: String) {
        preparationBehaviors.remove(behavior)
    }

    fun removePreparationAction(action: String) {
        preparationActions.remove(action)
    }

    fun setFear() {
        fear.value = !fear.value
    }

    fun setSadness() {
        sadness.value = !sadness.value
    }

    fun setAnxiety() {
        anxiety.value = !anxiety.value
    }

    fun setGuilt() {
        guilt.value = !guilt.value
    }

    fun setShame() {
        shame.value = !shame.value
    }

    fun setHappiness() {
        happiness.value = !happiness.value
    }

    fun addReviewThought(thought: String) {
        reviewThoughts.add(thought)
    }

    fun addReviewSensation(sensation: String) {
        reviewSensations.add(sensation)
    }

    fun addReviewBehavior(behavior: String) {
        reviewBehaviors.add(behavior)
    }

    fun removeReviewThought(thought: String) {
        reviewThoughts.remove(thought)
    }

    fun removeReviewSensation(sensation: String) {
        reviewSensations.remove(sensation)
    }

    fun removeReviewBehavior(behavior: String) {
        reviewBehaviors.remove(behavior)
    }

    fun setExperiencingRating(rating: Float) {
        experiencingRating.floatValue = rating
    }

    fun setAnchoringRating(rating: Float) {
        anchoringRating.floatValue = rating
    }

    fun setThinkingRating(rating: Float) {
        thinkingRating.floatValue = rating
    }

    fun setEngagingRating(rating: Float) {
        engagingRating.floatValue = rating
    }

    fun setLearnings(learnings: String) {
        this.learnings.value = learnings
    }

    fun addPreparation() {
        val title = title.value
        val description = description.value
        val preparation = Preparation(
            thoughts = preparationThoughts,
            interpretations = preparationInterpretations,
            behaviors = preparationBehaviors,
            actions = preparationActions
        )
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, title, description, preparation)
        }
    }

    fun markAsInProgress() {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, Status.IN_PROGRESS)
            notificationService.showReminderNotification(title.value, userId, exposureId)
        }
    }

    fun addReview() {
        val emotions = listOf(
            fear to Emotion.FEAR,
            sadness to Emotion.SADNESS,
            anxiety to Emotion.ANXIETY,
            guilt to Emotion.GUILT,
            shame to Emotion.SHAME,
            happiness to Emotion.HAPPINESS
        )
            .filter { it.first.value }
            .map { it.second }
        val review = Review(
            emotions = emotions,
            thoughts = reviewThoughts,
            sensations = reviewSensations,
            behaviors = reviewBehaviors,
            experiencing = experiencingRating.floatValue,
            anchoring = anchoringRating.floatValue,
            thinking = thinkingRating.floatValue,
            engaging = engagingRating.floatValue,
            learnings = learnings.value
        )
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, review)
        }
    }

    fun deleteExposure() {
        viewModelScope.launch {
            storageService.deleteExposure(userId, exposureId)
        }
    }
}