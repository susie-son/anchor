package com.susieson.anchor.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.model.AnchorUser
import com.susieson.anchor.ui.components.AnchorFloatingActionButton
import com.susieson.anchor.ui.components.AnchorTopAppBar
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.login.LoginScreen
import com.susieson.anchor.ui.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object ExposuresNav

@Serializable
data class ExposureNav(val exposureId: String)

@Serializable
object SettingsNav

data class AnchorScaffold(
    val topAppBar: AnchorTopAppBar,
    val floatingActionButton: AnchorFloatingActionButton? = null
) {
    companion object {
        val Default = AnchorScaffold(AnchorTopAppBar.Default)
    }
}

@Composable
fun AnchorNavHost(
    user: AnchorUser?,
    setScaffold: (AnchorScaffold) -> Unit,
    navController: NavHostController,
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
    NavHost(
        navController = navController,
        startDestination = ExposuresNav
    ) {
        composable<SettingsNav> {
            SettingsScreen(
                onNavigateUp = navController::navigateUp,
                setScaffold = setScaffold,
                modifier = modifier
            )
        }
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
