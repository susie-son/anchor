package com.susieson.anchor.ui.exposure

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.AnchorTopAppBarState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExposureReady(
    onBack: () -> Unit,
    onNext: () -> Unit,
    setTopAppBar: (AnchorTopAppBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    val checked = remember { mutableStateListOf(false, false) }

    val postNotificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    setTopAppBar(
        AnchorTopAppBarState(
            title = R.string.ready_top_bar_title,
            onBack = onBack
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.padding(16.dp)
    ) {
        postNotificationPermission?.let {
            if (!it.status.isGranted) {
                NotificationCard(postNotificationPermission = it)
            }
        }
        Text(
            stringResource(R.string.ready_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        ReadyCheckList(
            checked = checked,
            onCheckedChange = { index, value -> checked[index] = value }
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(horizontal = 16.dp)
        ) {
            OutlinedButton(onClick = onBack) { Text(stringResource(R.string.ready_dismiss_button)) }
            FilledTonalButton(
                onClick = onNext,
                enabled = checked.all { it }
            ) { Text(stringResource(R.string.ready_confirm_button)) }
        }
    }
}

@Composable
fun ReadyCheckList(
    modifier: Modifier = Modifier,
    checked: List<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(stringResource(R.string.ready_check_1)) },
            trailingContent = {
                Checkbox(
                    checked = checked[0],
                    onCheckedChange = { onCheckedChange(0, it) }
                )
            },
            modifier = modifier.height(40.dp)
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.ready_check_2)) },
            trailingContent = {
                Checkbox(
                    checked = checked[1],
                    onCheckedChange = { onCheckedChange(1, it) }
                )
            },
            modifier = modifier.height(40.dp)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationCard(modifier: Modifier = Modifier, postNotificationPermission: PermissionState) {
    val context = LocalContext.current
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
                onClick = {
                    if (postNotificationPermission.status == PermissionStatus.Denied(false)) {
                        openAppSettings(context)
                    } else {
                        postNotificationPermission.launchPermissionRequest()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.notification_permission_button))
            }
        }
    }
}

fun openAppSettings(context: Context) {
    val intent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
        } else {
            Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
        }
    context.startActivity(intent)
}
