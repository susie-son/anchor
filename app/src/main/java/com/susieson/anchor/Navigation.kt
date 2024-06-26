package com.susieson.anchor

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.exposures.ExposuresViewModel
import com.susieson.anchor.ui.login.LoginScreen
import com.susieson.anchor.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.typeOf

@Serializable
sealed class Destination {

    @Serializable
    data object Login : Destination()

    @Serializable
    data class LoggedIn(val userId: String) : Destination()

    @Serializable
    data class Exposures(val userId: String) : Destination()

    @Serializable
    data class Settings(val userId: String) : Destination()

    @Serializable
    data class ExposureDetails(val userId: String, val exposure: Exposure) : Destination()

    @Serializable
    data class ExposurePreparation(val userId: String, val exposure: Exposure) : Destination()

    @Serializable
    data class ExposureReady(val userId: String, val exposure: Exposure) : Destination()

    @Serializable
    data class ExposureReview(val userId: String, val exposure: Exposure) : Destination()

    @Serializable
    data class ExposureSummary(val userId: String, val exposure: Exposure) : Destination()

    companion object {
        fun fromRoute(route: String, args: Bundle?): Destination? {
            val subclass = Destination::class.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }
            return subclass?.let { createInstance(it, args) }
        }

        private fun <T : Any> createInstance(kClass: KClass<T>, bundle: Bundle?): T? {
            val constructor = kClass.primaryConstructor
            return constructor?.let {
                val args = it.parameters.associateWith { param ->
                    bundle?.get(param.name)
                }
                it.callBy(args)
            } ?: kClass.objectInstance
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = Destination.Login, modifier = modifier) {
        composable<Destination.Login> {
            LoginScreen(
                viewModel = hiltViewModel(),
                onLogin = { userId ->
                    navController.navigate(Destination.LoggedIn(userId))
                }
            )
        }
        navigation<Destination.LoggedIn>(startDestination = Destination.Exposures) {
            composable<Destination.Exposures> { backStackEntry ->
                val destination: Destination.Exposures = backStackEntry.toRoute()
                ExposuresScreen(
                    onItemSelect = { exposure ->
                        navController.navigate(
                            Destination.ExposureDetails(
                                destination.userId,
                                exposure
                            )
                        )
                    },
                    viewModel = hiltViewModel(
                        creationCallback = { factory: ExposuresViewModel.Factory ->
                            factory.create(destination.userId)
                        }
                    )
                )
            }
            composable<Destination.Settings> { backStackEntry ->
                val destination: Destination.Settings = backStackEntry.toRoute()
                SettingsScreen(
                    userId = destination.userId,
                    onNavigateUp = navController::navigateUp,
                    viewModel = hiltViewModel()
                )
            }
            navigation<Destination.ExposureDetails>(
                startDestination = Destination.ExposurePreparation,
                typeMap = mapOf(typeOf<Exposure>() to ExposureType)
            ) {
                composable<Destination.ExposurePreparation>(
                    typeMap = mapOf(typeOf<Exposure>() to ExposureType)
                ) { backStackEntry ->
                    val destination: Destination.ExposurePreparation = backStackEntry.toRoute()
                    ExposurePreparationScreen(
                        onNavigateUp = navController::navigateUp,
                        viewModel = hiltViewModel(
                            creationCallback = { factory: ExposurePreparationViewModel.Factory ->
                                factory.create(
                                    destination.userId,
                                    destination.exposure.id
                                )
                            }
                        )
                    )
                }
                composable<Destination.ExposureReady>(
                    typeMap = mapOf(typeOf<Exposure>() to ExposureType)
                ) { backStackEntry ->
                    val destination: Destination.ExposureReady = backStackEntry.toRoute()
                    ExposureReadyScreen(
                        exposure = destination.exposure,
                        viewModel = hiltViewModel(
                            creationCallback = { factory: ExposureReadyViewModel.Factory ->
                                factory.create(
                                    destination.userId,
                                    destination.exposure.id
                                )
                            }
                        )
                    )
                }
                composable<Destination.ExposureReview>(
                    typeMap = mapOf(typeOf<Exposure>() to ExposureType)
                ) { backStackEntry ->
                    val destination: Destination.ExposureReview = backStackEntry.toRoute()
                    ExposureReviewScreen(
                        onNavigateUp = navController::navigateUp,
                        viewModel = hiltViewModel(
                            creationCallback = { factory: ExposureReviewViewModel.Factory ->
                                factory.create(
                                    destination.userId,
                                    destination.exposure.id
                                )
                            }
                        )
                    )
                }
                composable<Destination.ExposureSummary>(
                    typeMap = mapOf(typeOf<Exposure>() to ExposureType)
                ) { backStackEntry ->
                    val exposureDetails: Destination.ExposureSummary = backStackEntry.toRoute()
                    ExposureSummaryScreen(
                        exposure = exposureDetails.exposure
                    )
                }
            }
        }
    }
}
