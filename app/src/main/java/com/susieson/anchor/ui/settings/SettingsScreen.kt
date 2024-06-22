package com.susieson.anchor.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.ui.AnchorScaffold
import com.susieson.anchor.ui.components.AnchorIconButton
import com.susieson.anchor.ui.components.AnchorTopAppBar
import com.susieson.anchor.ui.components.Loading

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsState(null)

    setScaffold(
        AnchorScaffold(
            topAppBar = AnchorTopAppBar(
                title = R.string.settings_top_bar_title,
                navigationIcon = AnchorIconButton(
                    onClick = onNavigateUp,
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = R.string.content_description_back
                )
            )
        )
    )

    if (user == null) {
        Loading(modifier = modifier)
        return
    }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

        when (user!!.isAnonymous) {
            true -> AnonymousSettings(
                modifier = modifier
            )

            false -> UserSettings(
                email = user!!.email!!,
                modifier = modifier
            )
        }

        Button(onClick = viewModel::logout, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_logout_button))
        }
        FilledTonalButton(onClick = viewModel::deleteAccount, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_delete_account_button))
        }
    }
}

@Composable
fun AnonymousSettings(
    modifier: Modifier = Modifier,
) {
    Text("Anonymous Settings")
}

@Composable
fun UserSettings(
    email: String,
    modifier: Modifier = Modifier,
) {
    Text(stringResource(R.string.user_settings_email_label, email))
}