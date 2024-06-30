package com.susieson.anchor.ui.exposure.ready

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.susieson.anchor.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExposureReadyContent(
    postNotificationPermissionStatus: PermissionStatus,
    acceptanceCriteriaChecked: List<Boolean>,
    onRequestPostNotificationPermission: () -> Unit,
    onOpenAppNotificationSettings: () -> Unit,
    onStartExposure: () -> Unit,
    onAcceptanceCriteriaCheckedChange: (Int, Boolean) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        NotificationPermissionCard(
            permissionStatus = postNotificationPermissionStatus,
            onRequestPermission = onRequestPostNotificationPermission,
            onOpenSettings = onOpenAppNotificationSettings,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = stringResource(R.string.ready_description),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        AcceptanceCriteriaChecklist(
            checked = acceptanceCriteriaChecked,
            onCheckedChange = onAcceptanceCriteriaCheckedChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = onStartExposure,
            enabled = acceptanceCriteriaChecked.all { it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.ready_confirm_button))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionCard(
    permissionStatus: PermissionStatus,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (permissionStatus.isGranted) return
    Card(modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.notification_permission_title),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.notification_permission_description),
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedButton(
                onClick = {
                    handleNotificationPermissionClick(
                        status = permissionStatus,
                        onRequestPermission = onRequestPermission,
                        onOpenSettings = onOpenSettings
                    )
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.notification_permission_button))
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun handleNotificationPermissionClick(
    status: PermissionStatus,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit
) {
    when (status) {
        is PermissionStatus.Denied -> {
            if (status.shouldShowRationale) {
                onRequestPermission()
            } else {
                onOpenSettings()
            }
        }
        else -> {
            onRequestPermission()
        }
    }
}

@Composable
private fun AcceptanceCriteriaChecklist(
    checked: List<Boolean>,
    onCheckedChange: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        ListItem(
            headlineContent = { Text(stringResource(R.string.ready_check_1)) },
            trailingContent = { Checkbox(checked[0], onCheckedChange = { onCheckedChange(0, it) }) },
            modifier = Modifier.height(40.dp)
        )
        ListItem(
            headlineContent = { Text(stringResource(R.string.ready_check_2)) },
            trailingContent = { Checkbox(checked[1], onCheckedChange = { onCheckedChange(1, it) }) },
            modifier = Modifier.height(40.dp)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
private fun ExposureReadyContentPreview() {
    ExposureReadyContent(
        postNotificationPermissionStatus = PermissionStatus.Denied(true),
        acceptanceCriteriaChecked = listOf(true, true),
        onRequestPostNotificationPermission = {},
        onOpenAppNotificationSettings = {},
        onStartExposure = {},
        onAcceptanceCriteriaCheckedChange = { _, _ -> },
        innerPadding = PaddingValues()
    )
}
