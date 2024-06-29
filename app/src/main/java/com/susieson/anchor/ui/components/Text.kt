package com.susieson.anchor.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorText(text: String, modifier: Modifier = Modifier, isError: Boolean = true) {
    if (isError) {
        Text(text, color = MaterialTheme.colorScheme.error, modifier = modifier)
    }
}

@Composable
fun ErrorBodyText(text: String, isError: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = if (isError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}

@Composable
fun ErrorLabelText(text: String, isError: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
fun LabelText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
fun BodyText(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.bodyMedium, modifier = modifier)
}

@Composable
fun TitleText(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.titleLarge, modifier = modifier)
}
