package com.omar.mentalcompanion.domain.services

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.omar.mentalcompanion.domain.workers.SyncDataWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncSchedulerService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun scheduleSyncService(hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        val syncRequest = PeriodicWorkRequestBuilder<SyncDataWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(target.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                SyncDataWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                syncRequest
            )
    }
}