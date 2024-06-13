package com.susieson.anchor

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.susieson.anchor.ui.exposure.ExposureScreen
import com.susieson.anchor.ui.exposures.ExposuresScreen
import com.susieson.anchor.ui.theme.AnchorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    return if (mainViewModel.userId.isNotBlank()) {
                        setContent {
                            AnchorTheme {
                                AnchorApp(mainViewModel.userId)
                            }
                        }
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )
    }
}

@Serializable
data class ExposuresNav(val userId: String)

@Serializable
data class ExposureNav(val userId: String, val exposureId: String?)

@Composable
fun AnchorApp(userId: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ExposuresNav(userId)) {
        composable<ExposuresNav> {
            ExposuresScreen(
                modifier = modifier,
                userId = userId,
                onStart = {
                    navController.navigate(ExposureNav(userId, null))
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
                userId = userId,
                exposureId = nav.exposureId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}