package com.susieson.anchor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.splash.SplashScreen
import com.susieson.anchor.ui.theme.AnchorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnchorTheme {
                AnchorApp()
            }
        }
    }
}

@Serializable
object SplashNav

@Serializable
data class ExposuresNav(val userId: String)

@Serializable
data class ExposureNav(val userId: String, val exposureId: String?)

@Composable
fun AnchorApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashNav) {
        composable<SplashNav> {
            SplashScreen(
                modifier = modifier,
                onStart = { userId -> navController.navigate(ExposuresNav(userId)) }
            )
        }
        composable<ExposuresNav> { backStackEntry ->
            val nav: ExposuresNav = backStackEntry.toRoute()
            ExposuresScreen(
                modifier = modifier,
                userId = nav.userId,
                onStart = {
                    navController.navigate(ExposureNav(nav.userId, null))
                },
                onItemClick = { userId, exposureId ->
                    navController.navigate(
                        ExposureNav(
                            userId,
                            exposureId
                        )
                    )
                }
            )
        }
        composable<ExposureNav>(
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://anchor.susieson.com/exposure/{userId}/{exposureId}"
            })
        ) { backStackEntry ->
            val nav: ExposureNav = backStackEntry.toRoute()
            ExposureScreen(
                modifier = modifier,
                userId = nav.userId,
                exposureId = nav.exposureId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    AnchorTheme {
        AnchorApp()
    }
}