package com.susieson.anchor.ui.exposure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.components.Loading

@Composable
fun ExposureScreen(
    exposureId: String,
    onBack: () -> Unit,
    setTopAppBar: (AnchorTopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExposureViewModel = hiltViewModel(),
) {
    val exposure = viewModel.exposure.value

    if (exposure == null) {
        Loading(modifier = modifier)
    } else when (exposure.status) {
        Status.DRAFT -> {
            PreparationForm(
                title = viewModel.title.value,
                description = viewModel.description.value,
                thoughts = viewModel.preparationThoughts,
                interpretations = viewModel.preparationInterpretations,
                behaviors = viewModel.preparationBehaviors,
                actions = viewModel.preparationActions,
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
                onDiscard = viewModel::deleteExposure,
                onBack = onBack,
                onNext = viewModel::addPreparation,
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }

        Status.READY -> {
            ExposureReady(
                onBack = onBack,
                onNext = {
                    viewModel.markAsInProgress()
                    onBack()
                },
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }

        Status.IN_PROGRESS -> {
            ReviewForm(
                fear = viewModel.fear.value,
                sadness = viewModel.sadness.value,
                anxiety = viewModel.anxiety.value,
                guilt = viewModel.guilt.value,
                shame = viewModel.shame.value,
                happiness = viewModel.happiness.value,
                thoughts = viewModel.reviewThoughts,
                sensations = viewModel.reviewSensations,
                behaviors = viewModel.reviewBehaviors,
                experiencingRating = viewModel.experiencingRating.floatValue,
                anchoringRating = viewModel.anchoringRating.floatValue,
                thinkingRating = viewModel.thinkingRating.floatValue,
                engagingRating = viewModel.engagingRating.floatValue,
                learnings = viewModel.learnings.value,
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
                onBack = onBack,
                onNext = viewModel::addReview,
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }

        Status.COMPLETED -> {
            ExposureSummary(
                exposure = exposure,
                onBack = onBack,
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }
    }

    LaunchedEffect(exposureId) {
        viewModel.getExposure(exposureId)
    }
}