package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R

data class AnchorTopAppBar(
    @StringRes
    val title: Int,
    val navigationIcon: AnchorIconButton? = null,
    val actions: List<AnchorIconButton>? = null
) {
    companion object {
        val Default = AnchorTopAppBar(R.string.app_name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopAppBar(state: AnchorTopAppBar, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(state.title)) },
        navigationIcon = {
            state.navigationIcon?.let { AnchorIconButton(it) }
        },
        actions = {
            state.actions?.forEach {
                AnchorIconButton(it)
            }
        },
        modifier = modifier
    )
}
