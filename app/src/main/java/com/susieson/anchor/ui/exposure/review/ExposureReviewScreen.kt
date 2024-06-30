package com.susieson.anchor.ui.exposure.review

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.susieson.anchor.ui.components.FormCloseHandler

@Composable
fun ExposureReviewScreen(
    viewModel: ExposureReviewViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            ExposureReviewTopBar(
                isFormEmpty = state.isEmpty,
                isFormValid = state.isValid,
                onNavigateUp = onNavigateUp,
                onAddReview = viewModel::addReview,
                onShowDiscardDialogChange = viewModel::onShowDiscardDialogChanged
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        ExposureReviewContent(
            emotions = state.emotions,
            thoughts = state.thoughts,
            sensations = state.sensations,
            behaviors = state.behaviors,
            experiencingRating = state.experiencing,
            anchoringRating = state.anchoring,
            thinkingRating = state.thinking,
            engagingRating = state.engaging,
            learnings = state.learnings,
            onEmotionChange = viewModel::onEmotionChanged,
            onThoughtChange = viewModel::onThoughtChanged,
            onSensationChange = viewModel::onSensationChanged,
            onBehaviorChange = viewModel::onBehaviorChanged,
            onExperiencingRatingChange = viewModel::onExperiencingRatingChanged,
            onAnchoringRatingChange = viewModel::onAnchoringRatingChanged,
            onThinkingRatingChange = viewModel::onThinkingRatingChanged,
            onEngagingRatingChange = viewModel::onEngagingRatingChanged,
            onLearningsChange = viewModel::onLearningsChanged,
            innerPadding = innerPadding
        )
    }
    FormCloseHandler(
        isFormEmpty = state.isEmpty,
        showDiscardDialog = state.showDiscardDialog,
        onDiscard = onNavigateUp,
        onShowDialog = viewModel::onShowDiscardDialogChanged
    )
}
