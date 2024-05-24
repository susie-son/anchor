package com.susieson.anchor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.susieson.anchor.ui.home.Home
import com.susieson.anchor.ui.home.HomeTopBar
import com.susieson.anchor.ui.preparation.Preparation
import com.susieson.anchor.ui.preparation.PreparationTopBar
import com.susieson.anchor.ui.theme.AnchorTheme

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
            Scaffold(modifier = modifier.fillMaxSize(), topBar = { HomeTopBar() }) { innerPadding ->
                Home(modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                    onStart = { navController.navigate("preparation") })
            }
        }
        composable("preparation") {
            Scaffold(modifier = modifier.fillMaxSize(),
                topBar = { PreparationTopBar(onBack = { navController.popBackStack() }) }) { innerPadding ->
                Preparation(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
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