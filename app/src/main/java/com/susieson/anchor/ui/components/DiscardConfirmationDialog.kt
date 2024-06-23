package com.susieson.anchor.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R
import com.susieson.anchor.ui.theme.AnchorTheme

@Composable
fun DiscardConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        text = { Text(stringResource(R.string.discard_dialog_text)) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) { Text(stringResource(R.string.discard_dialog_confirm)) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) { Text(stringResource(R.string.dialog_dismiss)) }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun DiscardConfirmationDialogPreview() {
    AnchorTheme {
        DiscardConfirmationDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}
