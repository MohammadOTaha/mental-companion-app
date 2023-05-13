package com.omar.mentalcompanion.domain.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.omar.mentalcompanion.domain.services.LocationBackgroundService

class BootCompletedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Intent(context, LocationBackgroundService::class.java).apply {
                action = LocationBackgroundService.ACTION_START
                context?.startService(this)
            }

            Log.d("132", "booted")
        }
    }
}