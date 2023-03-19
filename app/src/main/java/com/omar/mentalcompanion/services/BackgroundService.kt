package com.omar.mentalcompanion.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.omar.mentalcompanion.trackeddata.LocationLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class BackgroundService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var notificationService: NotificationService
    private lateinit var locationLiveData: LocationLiveData

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        notificationService = NotificationService(this)
        locationLiveData = LocationLiveData(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = notificationService.getNotification()

        locationLiveData.observeForever { location ->
            val lat = location.latitude.toString()
            val long = location.longitude.toString()
            val updatedNotification = notification.setContentText(
                "Location: ($lat, $long)"
            )
            notificationService.notificationManager.notify(1, updatedNotification.build())
        }

        startForeground(1, notification.build())
    }

    private fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }

        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_START_WITH_NOTIFICATION = "ACTION_START_WITH_NOTIFICATION"
        const val ACTION_STOP = "ACTION_STOP"
    }
}