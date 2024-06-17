package com.susieson.anchor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.susieson.anchor.MainViewModel
import com.susieson.anchor.ui.components.AnchorFloatingActionButton
import com.susieson.anchor.ui.components.AnchorTopAppBar

@Composable
fun AnchorApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val screenState by remember { viewModel.screenState }
    val user by viewModel.user.collectAsState(null)

    Scaffold(
        modifier = modifier,
        topBar = { AnchorTopAppBar(screenState.topAppBarState) },
        floatingActionButton = { screenState.fabState?.let { AnchorFloatingActionButton(it) } }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            AnchorNavHost(
                user = user,
                navController = navController,
                setScreenState = viewModel::setScreenState,
                modifier = modifier
            )
        }
    }
}
