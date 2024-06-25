package com.susieson.anchor.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.model.User
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.login.LoginScreen
import com.susieson.anchor.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object ExposuresNav

@Serializable
object SettingsNav

@Serializable
data class ExposureNav(val exposureId: String)

@Composable
fun NavHost(
    user: User?,
    onTopBarChange: (@Composable () -> Unit) -> Unit,
    onFloatingActionButtonChange: (@Composable () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val userId = user?.id
    if (userId == null) {
        LoginScreen(
            onTopBarChange = onTopBarChange,
            onFloatingActionButtonChange = onFloatingActionButtonChange,
            modifier = modifier
        )
        return
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ExposuresNav
    ) {
        composable<ExposuresNav> {
            ExposuresScreen(
                onTopBarChange = onTopBarChange,
                onFloatingActionButtonChange = onFloatingActionButtonChange,
                userId = userId,
                onItemSelect = { exposureId ->
                    navController.navigate(ExposureNav(exposureId))
                },
                onSettings = { navController.navigate(SettingsNav) },
                modifier = modifier
            )
        }
        composable<SettingsNav> {
            SettingsScreen(
                onTopBarChange = onTopBarChange,
                onFloatingActionButtonChange = onFloatingActionButtonChange,
                onNavigateUp = navController::navigateUp,
                modifier = modifier
            )
        }
        composable<ExposureNav>(
            deepLinks =
            listOf(
                navDeepLink {
                    uriPattern = "https://anchor.susieson.com/exposure/{userId}/{exposureId}"
                }
            )
        ) { backStackEntry ->
            val nav: ExposureNav = backStackEntry.toRoute()
            ExposureScreen(
                onTopBarChange = onTopBarChange,
                onFloatingActionButtonChange = onFloatingActionButtonChange,
                userId = userId,
                exposureId = nav.exposureId,
                onNavigateUp = navController::navigateUp,
                modifier = modifier
            )
        }
    }
}
