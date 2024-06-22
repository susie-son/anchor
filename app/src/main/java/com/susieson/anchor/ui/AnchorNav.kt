package com.susieson.anchor.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.model.AnchorUser
import com.susieson.anchor.ui.components.AnchorFabState
import com.susieson.anchor.ui.components.AnchorFormState
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object ExposuresNav

@Serializable
data class ExposureNav(val exposureId: String)

data class AnchorScreenState(
    val topAppBarState: AnchorTopAppBarState,
    val fabState: AnchorFabState? = null,
    val formState: AnchorFormState? = null,
    val canNavigateUp: Boolean = false
) {
    companion object {
        val Default = AnchorScreenState(
            topAppBarState = AnchorTopAppBarState.Default
        )
    }
}

@Composable
fun AnchorNavHost(
    user: AnchorUser?,
    setScreenState: (AnchorScreenState) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val userId = user?.id
    if (userId == null) {
        LoginScreen(modifier = modifier)
        return
    }
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
                setScreenState = setScreenState,
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
                onDiscard = navController::navigateUp,
                setScreenState = setScreenState,
                modifier = modifier
            )
        }
    }
}
