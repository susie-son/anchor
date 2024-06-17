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
import androidx.navigation.NavController
import com.susieson.anchor.R
import com.susieson.anchor.ui.AnchorScreenState

data class AnchorTopAppBarState(
    @StringRes
    val title: Int,
) {
    companion object {
        val Default =
            AnchorTopAppBarState(
                title = R.string.app_name,
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopAppBar(
    state: AnchorScreenState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(state.topAppBarState.title)) },
        navigationIcon = {
            if (state.canNavigateUp) {
                when (state.formState?.isEmpty) {
                    null -> IconButton(onClick = navController::navigateUp) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back)
                        )
                    }
                    true -> {
                        IconButton(onClick = navController::navigateUp) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.content_description_close)
                            )
                        }
                    }
                    false -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.content_description_close)
                            )
                        }
                    }
                }
            }
        },
        actions = {
            if (state.formState != null) {
                val (isValid, onSubmit) = state.formState
                IconButton(onClick = onSubmit, enabled = isValid) {
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
