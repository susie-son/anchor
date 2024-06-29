package com.susieson.anchor.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R
import com.susieson.anchor.ui.theme.AnchorTheme

@Composable
fun FormDiscardHandler(
    isEmpty: Boolean,
    showDiscardDialog: Boolean,
    onDiscard: () -> Unit,
    onShowDiscardDialog: (Boolean) -> Unit,
) {
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = onDiscard,
        onShowDiscardDialog = onShowDiscardDialog
    )
    DiscardDialog(
        show = showDiscardDialog,
        onDiscard = onDiscard,
        onSetShow = onShowDiscardDialog
    )
}

fun onClose(
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowDiscardDialog: (Boolean) -> Unit,
) {
    if (isEmpty) {
        onDiscard()
    } else {
        onShowDiscardDialog(true)
    }
}

fun onDone(
    onSubmit: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    onSubmit()
    onNavigateUp()
}

@Composable
private fun FormBackHandler(
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowDiscardDialog: (Boolean) -> Unit,
) {
    BackHandler {
        onClose(isEmpty, onDiscard, onShowDiscardDialog)
    }
}

@Composable
private fun DiscardDialog(
    show: Boolean,
    onDiscard: () -> Unit,
    onSetShow: (Boolean) -> Unit,
) {
    if (!show) return

    val onDismiss = { onSetShow(false) }
    val onConfirm = {
        onSetShow(false)
        onDiscard()
    }
    AlertDialog(
        text = { BodyText(stringResource(R.string.discard_dialog_text)) },
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onConfirm) { Text(stringResource(R.string.discard_dialog_confirm)) } },
        dismissButton = { TextButton(onDismiss) { Text(stringResource(R.string.dialog_dismiss)) } }
    )
}

@Preview(showBackground = true)
@Composable
private fun DiscardConfirmationDialogPreview() {
    AnchorTheme {
        DiscardDialog(
            show = true,
            onSetShow = {},
            onDiscard = {}
        )
    }
}
