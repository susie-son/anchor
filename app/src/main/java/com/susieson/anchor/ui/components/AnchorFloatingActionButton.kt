package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

data class AnchorFabState(
    @StringRes
    val text: Int,
    val onClick: () -> Unit,
    val icon: ImageVector
)

@Composable
fun AnchorFloatingActionButton(state: AnchorFabState, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = state.onClick,
        content = {
            Icon(
                state.icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(state.text))
        },
        modifier = modifier
    )
}
