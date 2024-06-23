package com.susieson.anchor.ui.components.form

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.AnchorIconButton
import com.susieson.anchor.ui.components.AnchorTopAppBar

@Composable
fun formTopAppBar(
    @StringRes
    title: Int,
    isValid: Boolean,
    isEmpty: Boolean,
    onDiscard: () -> Unit,
    onShowDiscardConfirmation: () -> Unit,
    onActionClick: () -> Unit,
) = AnchorTopAppBar(
    title = title,
    navigationIcon = AnchorIconButton(
        onClick = if (isEmpty) onDiscard else onShowDiscardConfirmation,
        icon = Icons.Default.Close,
        contentDescription = R.string.content_description_close
    ),
    actions = listOf(
        AnchorIconButton(
            onClick = onActionClick,
            icon = Icons.Default.Done,
            contentDescription = R.string.content_description_done,
            enabled = isValid
        )
    )
)
