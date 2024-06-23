package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.Loading
import com.susieson.anchor.ui.exposure.preparation.PreparationForm
import com.susieson.anchor.ui.exposure.review.ReviewForm

@Composable
fun ExposureScreen(
    userId: String,
    exposureId: String,
    setScaffold: (AnchorScaffold) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExposureViewModel = hiltViewModel()
) {
    val exposure by viewModel.getExposure(userId, exposureId).collectAsState(null)

    when (val state = getExposureState(exposure)) {
        is ExposureState.Loading -> Loading(modifier = modifier.fillMaxSize())
        is ExposureState.Draft -> {
            PreparationForm(
                userId = userId,
                exposureId = exposureId,
                onDiscard = {
                    onNavigateUp()
                    viewModel.deleteExposure(userId, state.exposureId)
                },
                addPreparation = viewModel::addPreparation,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
        is ExposureState.Ready -> {
            ExposureReady(
                userId = userId,
                exposureId = exposureId,
                title = state.title,
                onNavigateUp = onNavigateUp,
                markAsInProgress = viewModel::markAsInProgress,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
        is ExposureState.InProgress -> {
            ReviewForm(
                userId = userId,
                exposureId = exposureId,
                onDiscard = onNavigateUp,
                addReview = viewModel::addReview,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
        is ExposureState.Completed -> {
            ExposureSummary(
                exposure = state.exposure,
                onNavigateUp = onNavigateUp,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
    }
}
