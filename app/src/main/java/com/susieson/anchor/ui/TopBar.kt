package com.susieson.anchor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.susieson.anchor.Destination
import com.susieson.anchor.R
import com.susieson.anchor.ui.exposure.preparation.ExposurePreparationViewModel
import com.susieson.anchor.ui.exposure.review.ExposureReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    when (val route = currentRoute(navController)) {
        Destination.Login -> CenterAlignedTopAppBar({ Text(stringResource(R.string.app_name)) })

        is Destination.Exposures -> {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton({ navController.navigate(Destination.Settings) }) {
                        Icon(
                            Icons.Default.Settings,
                            stringResource(R.string.content_description_settings)
                        )
                    }
                }
            )
        }

        is Destination.Settings -> {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings_top_bar_title)) },
                navigationIcon = {
                    IconButton(navController::navigateUp) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.content_description_back)
                        )
                    }
                }
            )
        }

        is Destination.ExposurePreparation -> {
            val viewModel: ExposurePreparationViewModel = hiltViewModel(
                creationCallback = { factory: ExposurePreparationViewModel.Factory ->
                    factory.create(
                        userId = route.userId,
                        exposureId = route.exposure.id
                    )
                }
            )
            val isValid by remember { viewModel.isValid }
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.preparation_top_bar_title)) },
                navigationIcon = {
                    IconButton(viewModel::onClose) {
                        Icon(
                            Icons.Default.Close,
                            stringResource(R.string.content_description_close)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = viewModel::addPreparation,
                        enabled = isValid
                    ) {
                        Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
                    }
                }
            )
        }

        is Destination.ExposureReady -> {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.ready_top_bar_title)) },
                navigationIcon = {
                    IconButton(navController::navigateUp) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.content_description_back)
                        )
                    }
                },
            )
        }

        is Destination.ExposureReview -> {
            val viewModel: ExposureReviewViewModel = hiltViewModel()
            val isValid by remember { viewModel.isValid }
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.review_top_bar_title)) },
                navigationIcon = {
                    IconButton(viewModel::onClose) {
                        Icon(
                            Icons.Default.Close,
                            stringResource(R.string.content_description_close)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = viewModel::addReview,
                        enabled = isValid
                    ) {
                        Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
                    }
                }
            )
        }

        is Destination.ExposureSummary -> {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.summary_top_bar_title)) },
                navigationIcon = {
                    IconButton(navController::navigateUp) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.content_description_back)
                        )
                    }
                }
            )
        }

        else -> {
            CenterAlignedTopAppBar({ Text(stringResource(R.string.app_name)) })
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): Destination? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return Destination.fromRoute(
        route = navBackStackEntry?.destination?.route ?: "",
        args = navBackStackEntry?.arguments
    )
}
