package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledItemColumn(
    labelText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            labelText()
        }
        content()
    }
}

@Composable
fun LabeledItemRow(
    labelText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.weight(1f)) {
            labelText()
        }
        content()
    }
}

@Composable
fun LabeledItemWithSupporting(
    labelText: @Composable () -> Unit,
    supportingText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    LabeledItemColumn(labelText, modifier = modifier) {
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            supportingText()
        }
        content()
    }
}
