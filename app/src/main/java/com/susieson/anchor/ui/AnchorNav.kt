package com.susieson.anchor.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.model.AnchorUser
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
fun AnchorNavHost(
    user: AnchorUser?,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier
) {
    val userId = user?.id
    if (userId == null) {
        LoginScreen(
            setScaffold = setScaffold,
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
                userId = userId,
                onItemSelect = { exposureId ->
                    navController.navigate(ExposureNav(exposureId))
                },
                onSettings = { navController.navigate(SettingsNav) },
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
        composable<SettingsNav> {
            SettingsScreen(
                onNavigateUp = navController::navigateUp,
                setScaffold = setScaffold,
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
                userId = userId,
                exposureId = nav.exposureId,
                onNavigateUp = navController::navigateUp,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
    }
}
