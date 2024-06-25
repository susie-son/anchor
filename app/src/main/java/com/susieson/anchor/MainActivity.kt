package com.susieson.anchor

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.ui.components.NavHost
import com.susieson.anchor.ui.theme.AnchorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    return if (viewModel.isReady) {
                        setContent {
                            AnchorTheme {
                                val user by viewModel.user.collectAsState(null)
                                var topBar by remember {
                                    mutableStateOf<@Composable () -> Unit>(
                                        {
                                            CenterAlignedTopAppBar(
                                                title = {
                                                    Text(stringResource(R.string.app_name))
                                                }
                                            )
                                        }
                                    )
                                }
                                var fab by remember { mutableStateOf<@Composable () -> Unit>({}) }

                                Scaffold(
                                    topBar = topBar,
                                    floatingActionButton = fab,
                                    modifier = Modifier.fillMaxSize(),
                                ) { innerPadding ->
                                    NavHost(
                                        user = user,
                                        onTopBarChange = { topBar = it },
                                        onFloatingActionButtonChange = { fab = it },
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
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
