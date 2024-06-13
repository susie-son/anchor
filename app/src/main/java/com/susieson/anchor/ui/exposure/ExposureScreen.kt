package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.LoadingScreen

@Composable
fun ExposureScreen(
    modifier: Modifier = Modifier,
    userId: String,
    exposureId: String?,
    onBack: () -> Unit,
    exposureViewModel: ExposureViewModel = hiltViewModel()
) {
    val exposure by exposureViewModel.exposure.collectAsState()

    exposure?.let {
        when (it.status) {
            Status.DRAFT -> {
                PreparationScreen(
                    modifier = modifier,
                    onNext = { title, description, preparation ->
                        exposureViewModel.add(
                            userId,
                            it.id,
                            title,
                            description,
                            preparation
                        )
                    },
                    onBack = {
                        exposureViewModel.delete(userId, it.id)
                        onBack()
                    },
                )
            }

            Status.READY -> {
                ReadyScreen(
                    modifier = modifier,
                    onUpdate = { exposureViewModel.update(userId, it.id, it.title) },
                    onBack = onBack
                )
            }

            Status.IN_PROGRESS -> {
                ReviewScreen(
                    modifier = modifier,
                    onAdd = { review -> exposureViewModel.add(userId, it.id, review) },
                    onBack = onBack
                )
            }

            Status.COMPLETED -> {
                SummaryScreen(
                    modifier = modifier,
                    exposure = it,
                    onBack = onBack
                )
            }
        }
    } ?: LoadingScreen(
        modifier = modifier.fillMaxSize()
    )

    LaunchedEffect(userId, exposureId) {
        exposureViewModel.get(userId, exposureId)
    }
}