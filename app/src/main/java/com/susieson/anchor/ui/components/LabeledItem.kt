package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun LabeledItem(
    @StringRes
    label: Int,
    isOnSameLine: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit
) {
    val labelItem: @Composable (modifier: Modifier) -> Unit = {
        Text(
            stringResource(label),
            style = MaterialTheme.typography.labelLarge,
            color = color,
            modifier = it
        )
    }
    if (isOnSameLine) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            labelItem(Modifier.weight(1f))
            content()
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
        ) {
            labelItem(Modifier)
            content()
        }
    }
}
