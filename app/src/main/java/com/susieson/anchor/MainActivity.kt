package com.susieson.anchor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.preparation.PreparationScreen
import com.susieson.anchor.ui.ready.ReadyScreen
import com.susieson.anchor.ui.review.ReviewScreen
import com.susieson.anchor.ui.splash.SplashScreen
import com.susieson.anchor.ui.summary.SummaryScreen
import com.susieson.anchor.ui.theme.AnchorTheme
import dagger.hilt.android.AndroidEntryPoint

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

@Composable
fun AnchorApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                modifier = modifier,
                onStart = { userId -> navController.navigate("home/${userId}/exposures") }
            )
        }
        composable(
            route = "home/{userId}/exposures",
            arguments = listOf(navArgument("userId") {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->
            ExposuresScreen(
                modifier = modifier,
                userId = backStackEntry.arguments?.getString("userId")!!,
                onStart = { userId ->
                    navController.navigate("home/${userId}/exposures/preparation")
                },
                onItemClick = { userId, exposureId, status ->
                    when (status) {
                        Status.COMPLETED -> {
                            navController.navigate("home/${userId}/exposures/${exposureId}/summary")
                        }

                        Status.IN_PROGRESS -> {
                            navController.navigate("home/${userId}/exposures/${exposureId}/review")
                        }

                        Status.READY -> {
                            navController.navigate("home/${userId}/exposures/${exposureId}/ready")
                        }

                        Status.DRAFT -> {
                            navController.navigate("home/${userId}/exposures/${exposureId}/preparation")
                        }
                    }
                }
            )
        }
        composable(
            route = "home/{userId}/exposures/preparation",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            PreparationScreen(
                modifier = modifier,
                userId = backStackEntry.arguments?.getString("userId")!!,
                onBack = { navController.popBackStack() },
                onNext = { userId, exposureId ->
                    navController.navigate("home/${userId}/exposures/${exposureId}/ready", builder = { popUpTo("home/${userId}/exposures") })
                }
            )
        }
        composable(
            route = "home/{userId}/exposures/{exposureId}/ready",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("exposureId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            ReadyScreen(
                modifier = modifier,
                userId = backStackEntry.arguments?.getString("userId")!!,
                exposureId = backStackEntry.arguments?.getString("exposureId")!!,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "home/{userId}/exposures/{exposureId}/review",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("exposureId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            ReviewScreen(
                modifier = modifier,
                userId = backStackEntry.arguments?.getString("userId")!!,
                exposureId = backStackEntry.arguments?.getString("exposureId")!!,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "home/{userId}/exposures/{exposureId}/summary",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("exposureId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            SummaryScreen(
                modifier = modifier,
                userId = backStackEntry.arguments?.getString("userId")!!,
                exposureId = backStackEntry.arguments?.getString("exposureId")!!,
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