package com.omar.mentalcompanion.domain.services

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.omar.mentalcompanion.domain.workers.ReminderNotificationWorker
import com.omar.mentalcompanion.domain.workers.ReminderNotificationWorker.Companion.WORKER_NAME
import com.omar.mentalcompanion.domain.workers.ReminderNotificationWorker.Companion.WORKER_TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationSchedulerService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * @param hourOfDay the hour at which daily reminder notification should appear [0-23]
     * @param minute the minute at which daily reminder notification should appear [0-59]
     */
    fun scheduleReminderNotification(hourOfDay: Int, minute: Int, title: String, content: String) {
        Log.d("123", "Reminder scheduling request received for $hourOfDay:$minute")
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        Log.d("123", "Scheduling reminder notification for ${target.timeInMillis - System.currentTimeMillis()} ms from now")

        val notificationRequest = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(24, TimeUnit.HOURS)
            .addTag(WORKER_TAG)
            .setInitialDelay(target.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(
                ReminderNotificationWorker.createInputData(
                    title,
                    content
                )
            )
            .build()
        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                WORKER_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                notificationRequest
            )
    }

    fun cancelAll() {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORKER_TAG)
    }
}