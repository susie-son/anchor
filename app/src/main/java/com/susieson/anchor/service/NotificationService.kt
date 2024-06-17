package com.susieson.anchor.service

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
import kotlin.random.Random

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showReminderNotification(title: String, userId: String, exposureId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    NOTIFICATION_REMINDERS_CHANNEL_ID,
                    NOTIFICATION_REMINDERS_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationManager.createNotificationChannel(channel)
        }
        if (notificationManager.areNotificationsEnabled()) {
            val deepLinkIntent =
                Intent(
                    Intent.ACTION_VIEW,
                    "https://anchor.susieson.com/exposure/$userId/$exposureId".toUri(),
                    context,
                    MainActivity::class.java
                )

            val deepLinkPendingIntent: PendingIntent? =
                TaskStackBuilder.create(context).run {
                    addNextIntentWithParentStack(deepLinkIntent)
                    getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
                }

            val notification =
                NotificationCompat.Builder(context, NOTIFICATION_REMINDERS_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_notif)
                    .setContentTitle(title)
                    .setContentText(context.getString(R.string.reminder_notification_text))
                    .setContentIntent(deepLinkPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .build()
            notificationManager.notify(Random.nextInt(), notification)
        }
    }

    companion object {
        const val NOTIFICATION_REMINDERS_CHANNEL_ID = "anchor_notification_reminders"
        const val NOTIFICATION_REMINDERS_NAME = "Reminders"
    }
}
