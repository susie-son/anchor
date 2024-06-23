package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SummaryItem(
    @StringRes
    label: Int?,
    modifier: Modifier = Modifier,
    isOnSameLine: Boolean = false,
    content: @Composable () -> Unit
) {
    if (label != null) {
        LabeledItem(label, modifier = modifier, isOnSameLine = isOnSameLine) {
            content()
        }
    } else {
        Box(modifier = modifier) {
            content()
        }
    }
}

@Composable
fun CommaSeparatedListSummaryItem(
    list: List<String>,
    @StringRes
    label: Int?,
    modifier: Modifier = Modifier,
) {
    SummaryItem(label, modifier) {
        Text(
            list.joinToString(", "),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LineSeparatedListSummaryItem(
    list: List<String>,
    @StringRes
    label: Int?,
    modifier: Modifier = Modifier,
) {
    SummaryItem(label, modifier) {
        list.forEach {
            Text(it, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun RatingSummaryItem(
    rating: Float,
    @StringRes
    label: Int,
    modifier: Modifier = Modifier,
) {
    SummaryItem(label, modifier, isOnSameLine = true) {
        Text(
            rating.roundToInt().toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TextSummaryItem(
    text: String,
    @StringRes
    label: Int?,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    SummaryItem(label, modifier) {
        Text(text, style = textStyle)
    }
}

@Composable
fun SummarySection(items: List<@Composable ColumnScope.() -> Unit>, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach {
                it()
            }
        }
    }
}
