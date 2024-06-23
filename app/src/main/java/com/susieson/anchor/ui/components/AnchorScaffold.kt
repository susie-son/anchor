package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnchorScaffold(state: AnchorScaffold, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Scaffold(
        modifier = modifier,
        topBar = { AnchorTopAppBar(state.topAppBar) },
        floatingActionButton = {
            state.floatingActionButton?.let {
                AnchorFloatingActionButton(
                    it
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

data class AnchorScaffold(
    val topAppBar: AnchorTopAppBar,
    val floatingActionButton: AnchorFloatingActionButton? = null
) {
    companion object {
        val Default = AnchorScaffold(AnchorTopAppBar.Default)
    }
}
