package com.susieson.anchor.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.susieson.anchor.MainActivity
import com.susieson.anchor.R
import com.susieson.anchor.model.Exposure

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showReminderNotification(exposure: Exposure) {
        createNotificationChannel()
        if (notificationManager.areNotificationsEnabled()) {
            val notification = buildReminderNotification(exposure)
            notificationManager.notify(generateUniqueNotificationId(), notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_REMINDERS_CHANNEL_ID,
                NOTIFICATION_REMINDERS_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildReminderNotification(exposure: Exposure): Notification {
        val deepLinkIntent =
            Intent(
                Intent.ACTION_VIEW,
                "https://anchor.susieson.com".toUri(),
                context,
                MainActivity::class.java
            )

        val deepLinkPendingIntent: PendingIntent =
            TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

        return NotificationCompat.Builder(context, NOTIFICATION_REMINDERS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_notif)
            .setContentTitle(exposure.title)
            .setContentText(context.getString(R.string.reminder_notification_text))
            .setContentIntent(deepLinkPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setAutoCancel(true)
            .build()
    }

    private fun generateUniqueNotificationId(): Int {
        return System.currentTimeMillis().toInt()
    }

    companion object {
        const val NOTIFICATION_REMINDERS_CHANNEL_ID = "anchor_notification_reminders"
        const val NOTIFICATION_REMINDERS_NAME = "Reminders"
    }
}
