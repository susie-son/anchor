package com.susieson.anchor

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.susieson.anchor.ui.components.AnchorFloatingActionButton
import com.susieson.anchor.ui.components.AnchorTopAppBar
import com.susieson.anchor.ui.theme.AnchorTheme
import dagger.hilt.android.AndroidEntryPoint

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
                    return if (mainViewModel.isInitialized) {
                        setContent {
                            AnchorTheme {
                                AnchorApp(
                                    viewModel = mainViewModel,
                                    modifier = Modifier.fillMaxSize()
                                )
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

@Composable
fun AnchorApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val topAppBar by remember { viewModel.topAppBar }

    Scaffold(
        modifier = modifier,
        topBar = { AnchorTopAppBar(topAppBar) },
        floatingActionButton = {
            topAppBar.onAction?.let {
                AnchorFloatingActionButton(it)
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            AnchorNavHost(
                userId = viewModel.userId,
                setTopAppBar = viewModel::setTopAppBar,
                navController = navController,
                modifier = modifier
            )
        }
    }
}