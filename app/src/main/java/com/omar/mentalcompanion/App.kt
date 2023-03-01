package com.omar.mentalcompanion

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.omar.mentalcompanion.services.NotificationService

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        buildNotificationChannel()
    }

    private fun buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationService.CHANNEL_ID,
                NotificationService.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}