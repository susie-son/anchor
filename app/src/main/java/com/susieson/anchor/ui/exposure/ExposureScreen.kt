package com.susieson.anchor.ui.exposure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.AnchorScreenState
import com.susieson.anchor.ui.components.Loading

@Composable
fun ExposureScreen(
    userId: String,
    exposureId: String,
    setScreenState: (AnchorScreenState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExposureViewModel = hiltViewModel()
) {
    val exposure by viewModel.getExposure(userId, exposureId).collectAsState(null)

    when (exposure?.status) {
        null -> {
            Loading(modifier = modifier)
        }

        Status.DRAFT -> {
            PreparationScreenContent(
                viewModel = viewModel,
                onNext = { viewModel.addPreparation(userId, exposure!!.id) },
                setScreenState = setScreenState,
                modifier = modifier
            )
        }

        Status.READY -> {
            ExposureReady(
                onNext = {
                    viewModel.markAsInProgress(userId, exposure!!.id)
                },
                setScreenState = setScreenState,
                modifier = modifier
            )
        }

        Status.IN_PROGRESS -> {
            ReviewScreenContent(
                viewModel = viewModel,
                onNext = { viewModel.addReview(userId, exposure!!.id) },
                setScreenState = setScreenState,
                modifier = modifier
            )
        }

        Status.COMPLETED -> {
            ExposureSummary(
                exposure = exposure!!,
                setScreenState = setScreenState,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun PreparationScreenContent(
    viewModel: ExposureViewModel,
    onNext: () -> Unit,
    setScreenState: (AnchorScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
    val title by viewModel.title
    val description by viewModel.description
    val thoughts = viewModel.preparationThoughts
    val interpretations = viewModel.preparationInterpretations
    val behaviors = viewModel.preparationBehaviors
    val actions = viewModel.preparationActions

    PreparationForm(
        title = title,
        description = description,
        thoughts = thoughts.toList(),
        interpretations = interpretations.toList(),
        behaviors = behaviors.toList(),
        actions = actions.toList(),
        setTitle = viewModel::setTitle,
        setDescription = viewModel::setDescription,
        addThought = viewModel::addPreparationThought,
        addInterpretation = viewModel::addPreparationInterpretation,
        addBehavior = viewModel::addPreparationBehavior,
        addAction = viewModel::addPreparationAction,
        removeThought = viewModel::removePreparationThought,
        removeInterpretation = viewModel::removePreparationInterpretation,
        removeBehavior = viewModel::removePreparationBehavior,
        removeAction = viewModel::removePreparationAction,
        onNext = onNext,
        setScreenState = setScreenState,
        modifier = modifier
    )
}

@Composable
private fun ReviewScreenContent(
    viewModel: ExposureViewModel,
    onNext: () -> Unit,
    setScreenState: (AnchorScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
    val fear by viewModel.fear
    val sadness by viewModel.sadness
    val anxiety by viewModel.anxiety
    val guilt by viewModel.guilt
    val shame by viewModel.shame
    val happiness by viewModel.happiness

    val thoughts = viewModel.reviewThoughts
    val sensations = viewModel.reviewSensations
    val behaviors = viewModel.reviewBehaviors

    val experiencing by viewModel.experiencingRating
    val anchoring by viewModel.anchoringRating
    val thinking by viewModel.thinkingRating
    val engaging by viewModel.engagingRating

    val learnings by viewModel.learnings

    ReviewForm(
        fear = fear,
        sadness = sadness,
        anxiety = anxiety,
        guilt = guilt,
        shame = shame,
        happiness = happiness,
        thoughts = thoughts.toList(),
        sensations = sensations.toList(),
        behaviors = behaviors.toList(),
        experiencingRating = experiencing,
        anchoringRating = anchoring,
        thinkingRating = thinking,
        engagingRating = engaging,
        learnings = learnings,
        setFear = viewModel::setFear,
        setSadness = viewModel::setSadness,
        setAnxiety = viewModel::setAnxiety,
        setGuilt = viewModel::setGuilt,
        setShame = viewModel::setShame,
        setHappiness = viewModel::setHappiness,
        addThought = viewModel::addReviewThought,
        addSensation = viewModel::addReviewSensation,
        addBehavior = viewModel::addReviewBehavior,
        setExperiencingRating = viewModel::setExperiencingRating,
        setAnchoringRating = viewModel::setAnchoringRating,
        setThinkingRating = viewModel::setThinkingRating,
        setEngagingRating = viewModel::setEngagingRating,
        setLearnings = viewModel::setLearnings,
        removeThought = viewModel::removeReviewThought,
        removeSensation = viewModel::removeReviewSensation,
        removeBehavior = viewModel::removeReviewBehavior,
        onNext = onNext,
        setScreenState = setScreenState,
        modifier = modifier
    )
}
