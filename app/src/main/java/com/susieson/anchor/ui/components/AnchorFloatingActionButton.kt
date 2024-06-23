package com.susieson.anchor.ui.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnchorFloatingActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        content = {
            icon()
            text()
//            Icon(
//                state.icon,
//                contentDescription = state.contentDescription?.let { stringResource(it) },
//                modifier = Modifier.padding(end = 8.dp)
//            )
//            Text(stringResource(state.text))
        },
        modifier = modifier
    )
}
