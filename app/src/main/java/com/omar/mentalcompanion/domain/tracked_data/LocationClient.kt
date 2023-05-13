package com.omar.mentalcompanion.domain.tracked_data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.omar.mentalcompanion.domain.utils.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class LocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location> = callbackFlow {
        if (!context.hasLocationPermission()) {
            throw SecurityException("Location permission not granted")
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(!isGpsEnabled && !isNetworkEnabled) {
            throw SecurityException("Location services not enabled")
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)

                result.locations.lastOrNull()?.let { location ->
                    launch { send(location) }
                }
            }
        }

        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            client.removeLocationUpdates(locationCallback)
        }
    }

    companion object {
        private const val ONE_MINUTE: Long = 60 * 1000

        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 10 * ONE_MINUTE)
            .setMinUpdateIntervalMillis(10 * ONE_MINUTE)
            .setMinUpdateDistanceMeters(500f)
            .build()
    }
}