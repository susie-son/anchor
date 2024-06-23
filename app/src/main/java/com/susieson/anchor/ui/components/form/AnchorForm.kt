package com.susieson.anchor.ui.components.form

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.AnchorIconButton
import com.susieson.anchor.ui.components.AnchorTopAppBar

@Composable
fun FormTopAppBar(
    title: @Composable () -> Unit,
    isValid: Boolean,
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowDiscardConfirmation: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnchorTopAppBar(
        title = title,
        navigationIcon = {
            AnchorIconButton(
                onClick = if (isEmpty) onDiscard else onShowDiscardConfirmation,
                icon = { Icon(Icons.Default.Close, stringResource(R.string.content_description_close)) },
            )
        },
        actions = {
            AnchorIconButton(
                onClick = onActionClick,
                icon = { Icon(Icons.Default.Done, stringResource(R.string.content_description_done)) },
                enabled = isValid
            )
        },
        modifier = modifier,
    )
}
