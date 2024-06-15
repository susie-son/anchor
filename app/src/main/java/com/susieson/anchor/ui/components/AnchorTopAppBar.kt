package com.susieson.anchor.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R

data class AnchorTopAppBarState(
    @StringRes
    val title: Int,
    val formState: AnchorFormState? = null,
    val onBack: (() -> Unit)? = null,
    val onAction: (() -> Unit)? = null
) {
    companion object {
        val Default = AnchorTopAppBarState(
            title = R.string.app_name,
            formState = null,
            onBack = null,
            onAction = null
        )
    }
}

data class AnchorFormState(
    val isEmpty: Boolean,
    val onDiscard: () -> Unit,
    val isValid: Boolean,
    val onConfirm: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopAppBar(state: AnchorTopAppBarState, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(state.title)) },
        navigationIcon = {
            state.onBack?.let {
                if (state.formState == null) {
                    IconButton(onClick = it) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back)
                        )
                    }
                } else {
                    val (isEmpty, onDiscard) = state.formState
                    IconButton(onClick = { if (isEmpty) it() else onDiscard() }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.content_description_close)
                        )
                    }
                }
            }
        },
        actions = {
            if (state.formState != null) {
                val (_, _, isValid, onConfirm) = state.formState
                IconButton(onClick = onConfirm, enabled = isValid) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = stringResource(R.string.content_description_done)
                    )
                }
            }
        },
        modifier = modifier
    )
}