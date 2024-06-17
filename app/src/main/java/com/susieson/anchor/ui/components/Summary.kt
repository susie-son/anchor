package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
    label?.let {
        LabeledItem(it, modifier = modifier, isOnSameLine = isOnSameLine) {
            content()
        }
    } ?: Box(modifier = modifier) {
        content()
    }
}

@Composable
fun CommaSeparatedListSummaryItem(
    list: List<String>,
    modifier: Modifier = Modifier,
    @StringRes
    label: Int? = null
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
    modifier: Modifier = Modifier,
    @StringRes
    label: Int? = null
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
    modifier: Modifier = Modifier,
    @StringRes
    label: Int? = null
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
    modifier: Modifier = Modifier,
    @StringRes
    label: Int? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    SummaryItem(label, modifier) {
        Text(text, style = textStyle)
    }
}

@Composable
fun SummarySection(modifier: Modifier = Modifier, vararg items: @Composable () -> Unit) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach {
                it()
            }
        }
    }
}