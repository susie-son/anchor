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
    var scaffold by remember { mutableStateOf(AnchorScaffold.Default) }

    val user by viewModel.user.collectAsState(null)

    AnchorScaffold(state = scaffold, modifier = modifier) {
        AnchorNavHost(
            user = user,
            setScaffold = { scaffold = it }
        )
    }
}
