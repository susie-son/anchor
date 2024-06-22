package com.susieson.anchor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.susieson.anchor.MainViewModel
import com.susieson.anchor.ui.components.AnchorFloatingActionButton
import com.susieson.anchor.ui.components.AnchorTopAppBar

data class AnchorScaffold(
    val topAppBar: AnchorTopAppBar,
    val floatingActionButton: AnchorFloatingActionButton? = null
) {
    companion object {
        val Default = AnchorScaffold(AnchorTopAppBar.Default)
    }
}

@Composable
fun AnchorApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    var scaffold by remember { mutableStateOf(AnchorScaffold.Default) }

    val user by viewModel.user.collectAsState(null)

    Scaffold(
        modifier = modifier,
        topBar = { AnchorTopAppBar(scaffold.topAppBar) },
        floatingActionButton = {
            scaffold.floatingActionButton?.let {
                AnchorFloatingActionButton(
                    it
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            AnchorNavHost(
                user = user,
                setScaffold = { scaffold = it },
                modifier = modifier
            )
        }
    }
}
