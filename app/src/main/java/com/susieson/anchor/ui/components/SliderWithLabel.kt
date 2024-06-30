package com.susieson.anchor.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithLabel(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    steps: Int = 0,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    labelFormatter: (Float) -> String = { it.roundToInt().toString() }
) {
    val interactionSource = remember { MutableInteractionSource() }

    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.padding(vertical = 16.dp),
        interactionSource = interactionSource,
        steps = steps,
        valueRange = valueRange,
        thumb = {
            Column(
                modifier = Modifier.offset(y = (-16).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = labelFormatter(value),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(4.dp)
                )
                SliderDefaults.Thumb(interactionSource)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SliderWithLabelPreview() {
    SliderWithLabel(value = 0.5f, onValueChange = {})
}
