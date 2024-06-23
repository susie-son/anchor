package com.susieson.anchor.ui.components.form

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.susieson.anchor.ui.components.LabeledItem
import com.susieson.anchor.ui.components.LabeledSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormRatingItem(
    @StringRes
    label: Int,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    LabeledItem(
        label = label,
        modifier = modifier,
        color = if (value == 0f) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        isOnSameLine = false
    ) {
        LabeledSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..10f,
            steps = 9,
        )
    }
}
