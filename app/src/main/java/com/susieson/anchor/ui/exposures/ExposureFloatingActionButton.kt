package com.susieson.anchor.ui.exposures

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R

@Composable
fun ExposuresFloatingActionButton(
    modifier: Modifier = Modifier,
    onNavigateExposure: () -> Unit
) {
    ExtendedFloatingActionButton(onNavigateExposure, modifier = modifier) {
        Icon(Icons.Default.Add, null)
        Text(stringResource(R.string.exposures_start_button), modifier = Modifier.padding(start = 8.dp))
    }
}
