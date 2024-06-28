package com.susieson.anchor.ui.exposure.ready

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.susieson.anchor.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExposureReadyScreen(
    viewModel: ExposureReadyViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val isReadyChecked = remember { viewModel.checked }

    val postNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    Scaffold(
        topBar = { ExposureReadyTopBar(navController::navigateUp) },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            PostNotificationPermission(postNotificationPermission)
            Text(
                text = stringResource(R.string.ready_description),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            ReadyCheckList(
                checked = isReadyChecked,
                onCheckedChange = viewModel::onCheckedChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Button(
                onClick = {
                    viewModel.markAsInProgress()
                    navController.navigateUp()
                },
                enabled = isReadyChecked.all { it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text(stringResource(R.string.ready_confirm_button))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposureReadyTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.ready_top_bar_title)) },
        navigationIcon = {
            IconButton(onNavigateUp) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.content_description_back))
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PostNotificationPermission(state: PermissionState?) {
    state?.let {
        if (!it.status.isGranted) {
            NotificationCard(it, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun ReadyCheckList(
    checked: List<Boolean>,
    modifier: Modifier = Modifier,
    onCheckedChange: (Int, Boolean) -> Unit
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
@Composable
private fun NotificationCard(
    postNotificationPermission: PermissionState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.notification_permission_title), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.notification_permission_description))
            NotificationPermissionButton(postNotificationPermission, context, Modifier.align(Alignment.End))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionButton(
    postNotificationPermission: PermissionState,
    context: Context,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {
            when (postNotificationPermission.status) {
                is PermissionStatus.Denied -> {
                    if ((postNotificationPermission.status as PermissionStatus.Denied).shouldShowRationale) {
                        postNotificationPermission.launchPermissionRequest()
                    } else {
                        openAppSettings(context)
                    }
                }
                else -> {
                    postNotificationPermission.launchPermissionRequest()
                }
            }
        }
    ) {
        Text(stringResource(R.string.notification_permission_button))
    }
}

private fun openAppSettings(context: Context) {
    val intent = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
        }
        else -> {
            Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
        }
    }
    context.startActivity(intent)
}
