package com.susieson.anchor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import kotlinx.serialization.Serializable

@Serializable
data class ExposuresNav(val userId: String)

@Serializable
data class ExposureNav(val userId: String, val exposureId: String)

@Composable
fun AnchorNavHost(
    userId: String,
    setTopAppBar: (AnchorTopAppBarState) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ExposuresNav(userId)
    ) {
        composable<ExposuresNav> {
            ExposuresScreen(
                onItemSelect = { exposureId ->
                    navController.navigate(ExposureNav(userId, exposureId))
                },
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }
        composable<ExposureNav>(
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://anchor.susieson.com/exposure/{userId}/{exposureId}"
            })
        ) { backStackEntry ->
            val nav: ExposureNav = backStackEntry.toRoute()
            ExposureScreen(
                exposureId = nav.exposureId,
                onBack = navController::popBackStack,
                setTopAppBar = setTopAppBar,
                modifier = modifier
            )
        }
    }
}