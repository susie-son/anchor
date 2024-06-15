package com.susieson.anchor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.model.AnchorUser
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import kotlinx.serialization.Serializable

@Serializable
object ExposuresNav

@Serializable
data class ExposureNav(val exposureId: String)

@Composable
fun AnchorNavHost(
    user: AnchorUser?,
    setTopAppBar: (AnchorTopAppBarState) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val userId = user?.id ?: return // TODO: Show login screen
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
                setTopAppBar = setTopAppBar,
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
                onBack = navController::popBackStack,
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }
    }
}
