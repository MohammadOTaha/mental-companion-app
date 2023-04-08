package com.omar.mentalcompanion.domain.workers

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.omar.mentalcompanion.domain.services.NotificationService
import kotlin.random.Random

@Suppress("BlockingMethodInNonBlockingContext")
class QuestionnaireNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val notificationService = NotificationService(context)

    override suspend fun doWork(): Result {
        showNotification()

        Log.d("123", "doWork")


        return Result.success()
    }

    private fun showNotification() {
        val notification = notificationService.getNotification()
            .setContentTitle("Mental Companion")
            .setContentText("Please answer the questionnaire")
            .setAutoCancel(true)
            .build()

        notificationService.notificationManager.notify(1, notification)
    }
}