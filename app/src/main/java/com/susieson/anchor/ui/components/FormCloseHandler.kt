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
fun FormCloseHandler(
    isFormEmpty: Boolean,
    showDiscardDialog: Boolean,
    onDiscard: () -> Unit,
    onShowDialog: (Boolean) -> Unit,
) {
    FormBackHandler(isFormEmpty, onDiscard, onShowDialog)
    DiscardDialog(showDiscardDialog, onDiscard, onShowDialog)
}

fun onClose(isEmpty: Boolean, onDiscard: () -> Unit, onShowDiscardDialog: (Boolean) -> Unit) {
    if (isEmpty) {
        onDiscard()
    } else {
        onShowDiscardDialog(true)
    }
}

fun onDone(onSubmit: () -> Unit, onNavigateUp: () -> Unit) {
    onSubmit()
    onNavigateUp()
}

@Composable
private fun FormBackHandler(
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowDialog: (Boolean) -> Unit,
) {
    BackHandler {
        onClose(isEmpty, onDiscard, onShowDialog)
    }
}

@Composable
private fun DiscardDialog(
    show: Boolean,
    onDiscard: () -> Unit,
    onSetShow: (Boolean) -> Unit,
) {
    if (!show) return

    AlertDialog(
        title = { Text(stringResource(R.string.discard_dialog_title)) },
        text = { Text(stringResource(R.string.discard_dialog_text)) },
        onDismissRequest = { onSetShow(false) },
        confirmButton = {
            TextButton({
                onSetShow(false)
                onDiscard()
            }) {
                Text(stringResource(R.string.discard_dialog_confirm))
            }
        },
        dismissButton = { TextButton({ onSetShow(false) }) { Text(stringResource(R.string.dialog_dismiss)) } }
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
