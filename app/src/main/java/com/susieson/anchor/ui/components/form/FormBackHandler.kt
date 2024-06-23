package com.susieson.anchor.ui.components.form

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun FormBackHandler(
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowConfirmation: (Boolean) -> Unit,
) {
    BackHandler {
        if (isEmpty) {
            onDiscard()
        } else {
            onShowConfirmation(true)
        }
    }
}
