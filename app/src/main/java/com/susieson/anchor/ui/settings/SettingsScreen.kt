package com.susieson.anchor.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.susieson.anchor.ui.components.LoadingItem

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit,
    onNavigateLogin: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val error by viewModel.error.collectAsState(null)
    val user by viewModel.user.collectAsState(null)

    Scaffold(
        topBar = { SettingsTopBar(onNavigateUp) },
        modifier = modifier,
    ) { innerPadding ->
        when (val currentUser = user) {
            null -> LoadingItem(Modifier.fillMaxSize().padding(innerPadding))
            else -> SettingsContent(
                user = currentUser,
                error = error,
                onLinkAccount = viewModel::linkAccount,
                onAuthenticate = viewModel::authenticate,
                onLogout = viewModel::logout,
                onDeleteAccount = viewModel::deleteAccount,
                onNavigateLogin = onNavigateLogin,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
