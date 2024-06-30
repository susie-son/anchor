package com.susieson.anchor.ui.exposure.ready

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExposureReadyScreen(
    viewModel: ExposureReadyViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val postNotificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }
    val acceptanceCriteriaChecked = remember { viewModel.acceptanceCriteriaChecked }

    Scaffold(
        topBar = { ExposureReadyTopBar(onNavigateUp) },
        modifier = modifier,
    ) { innerPadding ->
        val context = LocalContext.current
        ExposureReadyContent(
            postNotificationPermissionStatus = postNotificationPermissionState?.status ?: PermissionStatus.Granted,
            acceptanceCriteriaChecked = acceptanceCriteriaChecked,
            onRequestPostNotificationPermission = { postNotificationPermissionState?.launchPermissionRequest() },
            onOpenAppNotificationSettings = { openAppNotificationSettings(context) },
            onStartExposure = {
                viewModel.startExposure()
                onNavigateUp()
            },
            onAcceptanceCriteriaCheckedChange = viewModel::onAcceptanceCriteriaCheckedChange,
            innerPadding = innerPadding
        )
    }
}

private fun openAppNotificationSettings(context: Context) {
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
