package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnchorScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = { AnchorTopAppBar() },
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = floatingActionButton
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}
