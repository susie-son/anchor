package com.susieson.anchor.ui.exposure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.Loading
import com.susieson.anchor.ui.exposure.preparation.PreparationForm

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

    when (exposure?.status) {
        null -> {
            Loading(modifier = modifier)
        }

        Status.DRAFT -> {
            PreparationForm(
                userId = userId,
                exposureId = exposureId,
                onDiscard = {
                    onNavigateUp()
                    viewModel.deleteExposure(userId, exposure!!.id)
                },
                addPreparation = viewModel::addPreparation,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }

        Status.READY -> {
            ExposureReady(
                userId = userId,
                exposureId = exposureId,
                title = exposure!!.title,
                onNavigateUp = onNavigateUp,
                markAsInProgress = viewModel::markAsInProgress,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }

        Status.IN_PROGRESS -> {
            ReviewForm(
                userId = userId,
                exposureId = exposureId,
                onDiscard = onNavigateUp,
                addReview = viewModel::addReview,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }

        Status.COMPLETED -> {
            ExposureSummary(
                exposure = exposure!!,
                onNavigateUp = onNavigateUp,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
    }
}
