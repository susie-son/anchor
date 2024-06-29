package com.susieson.anchor

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.ExposureType
import com.susieson.anchor.model.Status
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
import com.susieson.anchor.ui.settings.SettingsViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.KType
import kotlin.reflect.typeOf

sealed class Destination {
    @Serializable
    data object Login : Destination()

    @Serializable
    data class Exposures(val userId: String) : Destination()

    @Serializable
    data class Settings(val userId: String) : Destination()

    @Serializable
    data class ExposurePreparation(val userId: String) : Destination()

    @Serializable
    data class ExposureReady(val userId: String, val exposure: Exposure) : Destination()

    @Serializable
    data class ExposureReview(val userId: String, val exposure: Exposure) : Destination()

    @Serializable
    data class ExposureSummary(val exposure: Exposure) : Destination()
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = Destination.Login) {
        composable<Destination.Login> {
            LoginScreen(
                onNavigateExposures = {
                    navController.navigate(Destination.Exposures(it)) {
                        popUpTo(Destination.Login) { inclusive = true }
                    }
                }
            )
        }
        createComposable<Destination.Exposures, ExposuresViewModel, ExposuresViewModel.Factory>(
            creation = { factory, destination -> factory.create(destination.userId) },
            screen = { viewModel ->
                ExposuresScreen(
                    viewModel = viewModel,
                    onNavigateSettings = { userId -> navController.navigate(
                        Destination.Settings(
                            userId
                        )
                    ) },
                    onNavigateExposure = { userId, exposure ->
                        navController.navigate(
                            when (exposure.status) {
                                Status.DRAFT -> Destination.ExposurePreparation(userId)
                                Status.READY -> Destination.ExposureReady(userId, exposure)
                                Status.IN_PROGRESS -> Destination.ExposureReview(userId, exposure)
                                Status.COMPLETED -> Destination.ExposureSummary(exposure)
                            }
                        )
                    }
                )
            }
        )
        createComposable<Destination.Settings, SettingsViewModel, SettingsViewModel.Factory>(
            creation = { factory, destination -> factory.create(destination.userId) },
            screen = { viewModel ->
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateUp = navController::navigateUp,
                    onNavigateLogin = { userId ->
                        navController.navigate(Destination.Login) {
                            popUpTo(Destination.Exposures(userId)) { inclusive = true }
                        }
                    }
                )
            }
        )
        createComposable<Destination.ExposurePreparation, ExposurePreparationViewModel, ExposurePreparationViewModel.Factory>(
            creation = { factory, destination -> factory.create(destination.userId) },
            screen = { viewModel -> ExposurePreparationScreen(viewModel, navController::navigateUp) }
        )
        createComposable<Destination.ExposureReady, ExposureReadyViewModel, ExposureReadyViewModel.Factory>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType),
            creation = { factory, destination -> factory.create(destination.userId, destination.exposure) },
            screen = { viewModel -> ExposureReadyScreen(viewModel, navController::navigateUp) }
        )
        createComposable<Destination.ExposureReview, ExposureReviewViewModel, ExposureReviewViewModel.Factory>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType),
            creation = { factory, destination -> factory.create(destination.userId, destination.exposure) },
            screen = { viewModel -> ExposureReviewScreen(viewModel, navController::navigateUp) }
        )
        createComposable<Destination.ExposureSummary, ExposureSummaryViewModel, ExposureSummaryViewModel.Factory>(
            typeMap = mapOf(typeOf<Exposure>() to ExposureType),
            creation = { factory, destination -> factory.create(destination.exposure) },
            screen = { viewModel -> ExposureSummaryScreen(viewModel, navController::navigateUp) }
        )
    }
}

inline fun <reified T : Any, reified VM : ViewModel, reified F> NavGraphBuilder.createComposable(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    noinline screen: @Composable (viewModel: VM) -> Unit,
    noinline creation: (factory: F, destination: T) -> VM
) {
    composable<T>(typeMap = typeMap) { backStackEntry ->
        val destination: T = backStackEntry.toRoute()
        screen(
            hiltViewModel(
                creationCallback = { factory: F -> creation(factory, destination) }
            )
        )
    }
}
