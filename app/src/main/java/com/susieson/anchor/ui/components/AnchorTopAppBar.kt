package com.susieson.anchor.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = { Text(stringResource(R.string.app_name)) },
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier
    )
}
