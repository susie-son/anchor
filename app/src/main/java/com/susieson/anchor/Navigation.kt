package com.susieson.anchor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.ExposureType
import com.susieson.anchor.ui.exposure.preparation.ExposurePreparationScreen
import com.susieson.anchor.ui.exposure.preparation.ExposurePreparationViewModel
import com.susieson.anchor.ui.exposure.ready.ExposureReadyScreen
import com.susieson.anchor.ui.exposure.ready.ExposureReadyViewModel
import com.susieson.anchor.ui.exposure.review.ExposureReviewScreen
import com.susieson.anchor.ui.exposure.review.ExposureReviewViewModel
import com.susieson.anchor.ui.exposure.summary.ExposureSummaryScreen
import com.susieson.anchor.ui.exposure.summary.ExposureSummaryViewModel
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.exposures.ExposuresViewModel
import com.susieson.anchor.ui.login.LoginScreen
import com.susieson.anchor.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object Login

@Serializable
data class Exposures(val userId: String)

@Serializable
object Settings

@Serializable
data class ExposurePreparation(val userId: String)

@Serializable
data class ExposureReady(val userId: String, val exposure: Exposure)

@Serializable
data class ExposureReview(val userId: String, val exposure: Exposure)

@Serializable
data class ExposureSummary(val exposure: Exposure)

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = Login, modifier = modifier) {
        composable<Login> {
            LoginScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Exposures> { backStackEntry ->
            val destination: Exposures = backStackEntry.toRoute()
            ExposuresScreen(
                viewModel = hiltViewModel(
                    creationCallback = { factory: ExposuresViewModel.Factory ->
                        factory.create(userId = destination.userId)
                    }
                ),
                navController = navController
            )
        }
        composable<Settings> {
            SettingsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<ExposurePreparation>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType)
        ) { backStackEntry ->
            val destination: ExposurePreparation = backStackEntry.toRoute()
            ExposurePreparationScreen(
                viewModel = hiltViewModel(
                    creationCallback = { factory: ExposurePreparationViewModel.Factory ->
                        factory.create(userId = destination.userId)
                    }
                ),
                navController = navController
            )
        }
        composable<ExposureReady>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType)
        ) { backStackEntry ->
            val destination: ExposureReady = backStackEntry.toRoute()
            ExposureReadyScreen(
                viewModel = hiltViewModel(
                    creationCallback = { factory: ExposureReadyViewModel.Factory ->
                        factory.create(
                            userId = destination.userId,
                            exposure = destination.exposure
                        )
                    }
                ),
                navController = navController
            )
        }
        composable<ExposureReview>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType)
        ) { backStackEntry ->
            val destination: ExposureReview = backStackEntry.toRoute()
            ExposureReviewScreen(
                viewModel = hiltViewModel(
                    creationCallback = { factory: ExposureReviewViewModel.Factory ->
                        factory.create(
                            userId = destination.userId,
                            exposure = destination.exposure
                        )
                    }
                ),
                navController = navController
            )
        }
        composable<ExposureSummary>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType)
        ) { backStackEntry ->
            val destination: ExposureSummary = backStackEntry.toRoute()
            ExposureSummaryScreen(
                viewModel = hiltViewModel(
                    creationCallback = { factory: ExposureSummaryViewModel.Factory ->
                        factory.create(exposure = destination.exposure)
                    }
                ),
                navController = navController
            )
        }
    }
}
