package com.susieson.anchor.ui.exposure

import android.Manifest
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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.susieson.anchor.R
import com.susieson.anchor.TopAppBarState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReadyScreen(
    modifier: Modifier = Modifier,
    onUpdate: () -> Unit,
    onBack: () -> Unit,
    onTopAppBarStateChanged: (TopAppBarState) -> Unit
) {
    var checked by rememberSaveable { mutableStateOf(listOf(false, false)) }

    val postNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    onTopAppBarStateChanged(
        TopAppBarState(
            title = R.string.ready_top_bar_title,
            onBack = onBack
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        postNotificationPermission?.let {
            if (!it.status.isGranted) {
                Card(modifier = modifier.fillMaxWidth()) {
                    Column(
                        modifier = modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.notification_permission_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(stringResource(R.string.notification_permission_description))
                        OutlinedButton(
                            onClick = { postNotificationPermission.launchPermissionRequest() },
                            modifier = modifier.align(Alignment.End)
                        ) {
                            Text(stringResource(R.string.notification_permission_button))
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
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedButton(onClick = onBack) { Text(stringResource(R.string.ready_dismiss_button)) }
            FilledTonalButton(
                onClick = {
                    onUpdate()
                    onBack()
                },
                enabled = checked.all { it }) { Text(stringResource(R.string.ready_confirm_button)) }
        }
    }
}