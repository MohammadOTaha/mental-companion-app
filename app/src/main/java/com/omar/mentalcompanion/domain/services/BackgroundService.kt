package com.omar.mentalcompanion.domain.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.data.entities.Location
import com.omar.mentalcompanion.domain.repositories.LocationRepository
import com.omar.mentalcompanion.domain.tracked_data.LocationLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class BackgroundService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var notificationService: NotificationService
    private lateinit var locationLiveData: LocationLiveData
    @Inject
    lateinit var locationRepository: LocationRepository

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

            FirebaseFirestore.getInstance().collection("location").add(
                hashMapOf(
                    "lat" to lat,
                    "lon" to long
                )
            )

            insertLocation(Location(lat.toDouble(), long.toDouble(), 0.0f, 0))

            notificationService.notificationManager.notify(Random.nextInt(), updatedNotification.build())
        }

        startForeground(1, notification.build())
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
        const val ACTION_START_WITH_NOTIFICATION = "ACTION_START_WITH_NOTIFICATION"
        const val ACTION_STOP = "ACTION_STOP"
    }
}