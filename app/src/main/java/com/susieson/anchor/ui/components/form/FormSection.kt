package com.susieson.anchor.ui.components.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun FormSection(vararg items: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach {
            it()
        }
    }
}

@Composable
fun LabeledFormSection(
    @StringRes
    label: Int,
    @StringRes
    descriptionLabel: Int?,
    vararg items: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.labelLarge
        )
        descriptionLabel?.let {
            Text(
                text = stringResource(it),
                style = MaterialTheme.typography.bodySmall
            )
        }
        items.forEach {
            it()
        }
    }
}
