package com.susieson.anchor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.susieson.anchor.R
import kotlin.random.Random

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showReminderNotification(title: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_REMINDERS_CHANNEL_ID,
                NOTIFICATION_REMINDERS_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        if (notificationManager.areNotificationsEnabled()) {
            val notification =
                NotificationCompat.Builder(context, NOTIFICATION_REMINDERS_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText("Remember to stay anchored during your exposure.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .build()
            notificationManager.notify(Random.nextInt(), notification)
        }
    }

    companion object {
        const val NOTIFICATION_REMINDERS_CHANNEL_ID = "anchor_notification_reminders"
        const val NOTIFICATION_REMINDERS_NAME = "Reminders"
    }
}