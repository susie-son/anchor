package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.TopAppBarState
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.LoadingScreen

@Composable
fun ExposureScreen(
    modifier: Modifier = Modifier,
    userId: String,
    exposureId: String?,
    onBack: () -> Unit,
    onTopAppBarStateChanged: (TopAppBarState) -> Unit,
    exposureViewModel: ExposureViewModel = hiltViewModel()
) {
    val exposureState by exposureViewModel.exposure.collectAsState()

    val exposure = exposureState

    if (exposure == null) {
        LoadingScreen(modifier = modifier.fillMaxSize())
    } else when (exposure.status) {
        Status.DRAFT -> {
            PreparationScreen(
                modifier = modifier,
                onNext = { title, description, preparation ->
                    exposureViewModel.add(
                        userId,
                        exposure.id,
                        title,
                        description,
                        preparation
                    )
                },
                onBack = {
                    exposureViewModel.delete(userId, exposure.id)
                    onBack()
                },
                onTopAppBarStateChanged = onTopAppBarStateChanged
            )
        }

        Status.READY -> {
            ReadyScreen(
                modifier = modifier,
                onUpdate = { exposureViewModel.update(userId, exposure.id, exposure.title) },
                onBack = onBack,
                onTopAppBarStateChanged = onTopAppBarStateChanged
            )
        }

        Status.IN_PROGRESS -> {
            ReviewScreen(
                modifier = modifier,
                onAdd = { review -> exposureViewModel.add(userId, exposure.id, review) },
                onBack = onBack,
                onTopAppBarStateChanged = onTopAppBarStateChanged
            )
        }

        Status.COMPLETED -> {
            SummaryScreen(
                modifier = modifier,
                exposure = exposure,
                onBack = onBack,
                onTopAppBarStateChanged = onTopAppBarStateChanged
            )
        }
    }

    LaunchedEffect(userId, exposureId) {
        exposureViewModel.get(userId, exposureId)
    }
}