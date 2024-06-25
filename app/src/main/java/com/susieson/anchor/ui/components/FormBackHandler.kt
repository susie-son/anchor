package com.susieson.anchor.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun FormBackHandler(
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowDiscardDialog: (Boolean) -> Unit,
) {
    BackHandler {
        if (isEmpty) {
            onDiscard()
        } else {
            onShowDiscardDialog(true)
        }
    }
}
