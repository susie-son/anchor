package com.susieson.anchor.ui.ready

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadyTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.ready_top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "Close"
                )
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReadyScreen(
    modifier: Modifier = Modifier,
    userId: String,
    exposureId: String,
    onBack: () -> Unit = {},
    readyViewModel: ReadyViewModel = hiltViewModel()
) {
    var checked by rememberSaveable { mutableStateOf(listOf(false, false)) }

    val postNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        ReadyTopBar(
            onBack = onBack,
        )
    }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            postNotificationPermission?.let {
                if (!it.status.isGranted) {
                    Card(modifier = modifier.fillMaxWidth()) {
                        Column(
                            modifier = modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Stay anchored \u2693", style = MaterialTheme.typography.titleMedium)
                            Text("Enable notifications to get an ongoing reminder about your exposure!")
                            OutlinedButton(
                                onClick = { postNotificationPermission.launchPermissionRequest() },
                                modifier = modifier.align(Alignment.End)
                            ) {
                                Text("Remind me")
                            }
                        }
                    }
                }
            }
            Text(
                stringResource(R.string.ready_description),
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Column {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.ready_check_1)) },
                    trailingContent = {
                        Checkbox(
                            checked = checked[0],
                            onCheckedChange = { checked = listOf(it, checked[1]) })
                    },
                    modifier = modifier.height(40.dp)
                )
                ListItem(
                    headlineContent = { Text(stringResource(R.string.ready_check_2)) },
                    trailingContent = {
                        Checkbox(
                            checked = checked[1],
                            onCheckedChange = { checked = listOf(checked[0], it) })
                    },
                    modifier = modifier.height(40.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                OutlinedButton(onClick = onBack) { Text(stringResource(R.string.ready_dismiss_button)) }
                FilledTonalButton(
                    onClick = {
                        readyViewModel.update(userId, exposureId)
                        onBack()
                    },
                    enabled = checked.all { it }) { Text(stringResource(R.string.ready_confirm_button)) }
            }
        }
    }
}