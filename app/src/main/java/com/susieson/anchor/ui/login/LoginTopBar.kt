package com.susieson.anchor.ui.login

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        modifier = modifier
    )
}

@Preview
@Composable
private fun LoginTopBarPreview() {
    LoginTopBar()
}
