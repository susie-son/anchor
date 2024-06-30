package com.susieson.anchor.ui.exposure.summary

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposureSummaryTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.summary_top_bar_title)) },
        navigationIcon = {
            IconButton(onNavigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    stringResource(R.string.content_description_back)
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ExposureSummaryTopBarPreview() {
    ExposureSummaryTopBar(onNavigateUp = {})
}
