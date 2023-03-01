package com.omar.mentalcompanion.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.omar.mentalcompanion.MainActivity

class NotificationService(private val context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotification(): NotificationCompat.Builder {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        return NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setContentTitle("Mental Companion")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
    }

    companion object {
        const val CHANNEL_ID = "mental_companion_notification_channel"
        const val CHANNEL_NAME = "Mental Companion"
    }
}