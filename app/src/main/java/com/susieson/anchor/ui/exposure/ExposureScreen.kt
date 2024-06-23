package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.ui.components.Loading
import com.susieson.anchor.ui.exposure.preparation.PreparationForm
import com.susieson.anchor.ui.exposure.review.ReviewForm

@Composable
fun ExposureScreen(
    onTopBarChange: (@Composable () -> Unit) -> Unit,
    onFloatingActionButtonChange: (@Composable () -> Unit) -> Unit,
    userId: String,
    exposureId: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExposureViewModel = hiltViewModel()
) {
    val exposure by viewModel.getExposure(userId, exposureId).collectAsState(null)

    onFloatingActionButtonChange {}

    when (val state = getExposureState(exposure)) {
        is ExposureState.Loading -> Loading(modifier = modifier.fillMaxSize())
        is ExposureState.Draft -> {
            PreparationForm(
                onTopBarChange = onTopBarChange,
                userId = userId,
                exposureId = exposureId,
                onDiscard = {
                    onNavigateUp()
                    viewModel.deleteExposure(userId, state.exposureId)
                },
                addPreparation = viewModel::addPreparation,
                modifier = modifier
            )
        }
        is ExposureState.Ready -> {
            ExposureReady(
                onTopBarChange = onTopBarChange,
                userId = userId,
                exposureId = exposureId,
                title = state.title,
                onNavigateUp = onNavigateUp,
                markAsInProgress = viewModel::markAsInProgress,
                modifier = modifier
            )
        }
        is ExposureState.InProgress -> {
            ReviewForm(
                onTopBarChange = onTopBarChange,
                userId = userId,
                exposureId = exposureId,
                onDiscard = onNavigateUp,
                addReview = viewModel::addReview,
                modifier = modifier
            )
        }
        is ExposureState.Completed -> {
            ExposureSummary(
                onTopBarChange = onTopBarChange,
                exposure = state.exposure,
                onNavigateUp = onNavigateUp,
                modifier = modifier
            )
        }
    }
}
