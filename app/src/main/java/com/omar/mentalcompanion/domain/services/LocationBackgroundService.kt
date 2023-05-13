package com.omar.mentalcompanion.domain.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.omar.mentalcompanion.R
import com.omar.mentalcompanion.data.entities.Location
import com.omar.mentalcompanion.domain.repositories.LocationRepository
import com.omar.mentalcompanion.domain.tracked_data.LocationClient
import com.omar.mentalcompanion.domain.utils.toFormattedTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationBackgroundService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    @Inject
    lateinit var locationRepository: LocationRepository

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        locationClient = LocationClient(
            this,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notificationBuilder = NotificationService.getNotificationBuilder(this)
            .setContentTitle("Application Running")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)

        val notificationManager = NotificationService.getNotificationManager(this)

        locationClient
            .getLocationUpdates()
            .catch { e ->
                notificationBuilder.setContentText("Location: ${e.message}")
                notificationManager.notify(1, notificationBuilder.build())
            }
            .onEach { location ->
                locationRepository.insertLocation(
                    Location(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        created_at = System.currentTimeMillis().toFormattedTimeString()
                    )
                )
            }
            .launchIn(serviceScope)

        startForeground(1, notificationBuilder.build())
    }

    private fun insertLocation(location: Location) {
        serviceScope.launch {
            locationRepository.insertLocation(location)
        }
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
        const val ACTION_STOP = "ACTION_STOP"
    }
}