package com.susieson.anchor

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
                    return if (mainViewModel.isInitialized) {
                        setContent {
                            AnchorTheme {
                                AnchorApp(viewModel = mainViewModel)
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
data class ExposureNav(val userId: String, val exposureId: String)

data class TopAppBarState(
    @StringRes
    val title: Int,
    val formState: FormState? = null,
    val onBack: (() -> Unit)? = null,
    val onAction: (() -> Unit)? = null
) {
    companion object {
        val Default = TopAppBarState(
            title = R.string.app_name,
            formState = null,
            onBack = null,
            onAction = null
        )
    }
}

data class FormState(
    val isEmpty: Boolean,
    val onDiscard: () -> Unit,
    val isValid: Boolean,
    val onConfirm: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopAppBar(state: TopAppBarState) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(state.title)) },
        navigationIcon = {
            state.onBack?.let {
                if (state.formState == null) {
                    IconButton(onClick = it) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_close),
                            contentDescription = "Close"
                        )
                    }
                } else {
                    val (isEmpty, onDiscard) = state.formState
                    IconButton(onClick = { if (isEmpty) it() else onDiscard() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_close),
                            contentDescription = "Close"
                        )
                    }
                }
            }
        },
        actions = {
            if (state.formState != null) {
                val (_, _, isValid, onConfirm) = state.formState
                IconButton(onClick = onConfirm, enabled = isValid) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_done),
                        contentDescription = "Done"
                    )
                }
            }
        },
        modifier = Modifier
    )
}

@Composable
fun AnchorApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val topAppBar by remember { viewModel.topAppBar }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AnchorTopAppBar(topAppBar) },
        floatingActionButton = {
            topAppBar.onAction?.let {
                ExtendedFloatingActionButton(
                    onClick = it,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_add),
                            contentDescription = null,
                            modifier = modifier.padding(end = 8.dp)
                        )
                        Text(stringResource(R.string.exposures_start_button))
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(navController = navController, startDestination = ExposuresNav(viewModel.userId)) {
                composable<ExposuresNav> {
                    ExposuresScreen(
                        modifier = modifier,
                        onItemSelect = { exposureId ->
                            navController.navigate(ExposureNav(viewModel.userId, exposureId))
                        },
                        setTopAppBarState = viewModel::setTopAppBar
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
                        exposureId = nav.exposureId,
                        onBack = navController::popBackStack,
                        setTopAppBarState = viewModel::setTopAppBar
                    )
                }
            }
        }
    }
}