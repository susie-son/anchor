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
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.home.HomeScreen
import com.susieson.anchor.ui.preparation.PreparationScreen
import com.susieson.anchor.ui.review.ReviewScreen
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

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                modifier = modifier,
                onStart = { navController.navigate("preparation") },
                onItemClick = { exposure ->
                    when (exposure.status) {
                        Status.COMPLETED -> {
                            navController.navigate("summary/${exposure.id}")
                        }
                        Status.IN_PROGRESS -> {
                            navController.navigate("review/${exposure.id}")
                        }
                        else -> {}
                    }

                }
            )
        }
        composable("preparation") {
            PreparationScreen(
                modifier = modifier,
                onBack = { navController.navigateUp() }
            )
        }
        composable("review/{exposureId}") { backStackEntry ->
            ReviewScreen(
                modifier = modifier,
                exposureId = backStackEntry.arguments?.getString("exposureId")!!,
                onBack = { navController.navigateUp() }
            )
        }
        composable("summary/{exposureId}") { backStackEntry ->
            SummaryScreen(
                modifier = modifier,
                exposureId = backStackEntry.arguments?.getString("exposureId")!!,
                onBack = { navController.navigateUp() }
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