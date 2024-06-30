package com.susieson.anchor.ui.exposures

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposuresTopBar(
    modifier: Modifier = Modifier,
    onNavigateSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onNavigateSettings) {
                Icon(Icons.Default.Settings, stringResource(R.string.content_description_settings))
            }
        },
        modifier = modifier
    )
}
