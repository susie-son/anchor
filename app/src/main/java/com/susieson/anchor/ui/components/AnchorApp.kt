package com.susieson.anchor.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.susieson.anchor.MainViewModel

@Composable
fun AnchorApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val user by viewModel.user.collectAsState(null)
    var topBar by remember { mutableStateOf<@Composable () -> Unit>({ AnchorTopAppBar() }) }
    var fab by remember { mutableStateOf<@Composable () -> Unit>({}) }

    AnchorScaffold(
        topBar = topBar,
        floatingActionButton = fab,
        modifier = modifier
    ) {
        AnchorNavHost(
            user = user,
            onTopBarChange = { topBar = it },
            onFloatingActionButtonChange = { fab = it },
        )
    }
}
