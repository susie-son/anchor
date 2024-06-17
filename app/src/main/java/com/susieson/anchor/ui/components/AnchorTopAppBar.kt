package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R

data class AnchorTopAppBarState(
    @StringRes
    val title: Int,
    val formState: AnchorFormState? = null,
    val onAction: (() -> Unit)? = null
) {
    companion object {
        val Default =
            AnchorTopAppBarState(
                title = R.string.app_name,
                formState = null,
                onAction = null
            )
    }
}

data class AnchorFormState(
    val isEmpty: Boolean,
    val isValid: Boolean,
    val onConfirm: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopAppBar(state: AnchorTopAppBarState, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(state.title)) },
        navigationIcon = {

        },
        actions = {

        },
        modifier = modifier
    )
}
