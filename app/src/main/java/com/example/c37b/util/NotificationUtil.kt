package com.example.c37b.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.c37b.R

fun showServiceReminderNotification(context: Context, bikeName: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = "bike_service_reminder_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Bike Service Reminders", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Bike Service Reminder")
        .setContentText("It's time to service your $bikeName!")
        .setSmallIcon(R.drawable.ic_notification)
        .build()

    notificationManager.notify(1, notification)
}
