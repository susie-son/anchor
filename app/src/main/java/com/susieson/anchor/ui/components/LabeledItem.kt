package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledItem(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        label()
        content()
    }
}

@Composable
fun SameLineLabeledItem(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.weight(1f)) {
            label()
        }
        content()
    }
}

@Composable
fun LabeledItemWithSupporting(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    supporting: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    LabeledItem(modifier, label) {
        supporting()
        content()
    }
}
