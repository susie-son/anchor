package com.susieson.anchor.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnchorIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(onClick = onClick, enabled = enabled, modifier = modifier) {
        icon()
//        Icon(
//            state.icon,
//            contentDescription = stringResource(state.contentDescription)
//        )
    }
}
