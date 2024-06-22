package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

data class AnchorIconButton(
    val onClick: () -> Unit,
    val icon: ImageVector,
    @StringRes
    val contentDescription: Int,
    val enabled: Boolean = true
)

@Composable
fun AnchorIconButton(state: AnchorIconButton, modifier: Modifier = Modifier) {
    IconButton(onClick = state.onClick, enabled = state.enabled, modifier = modifier) {
        Icon(
            state.icon,
            contentDescription = stringResource(state.contentDescription)
        )
    }
}
