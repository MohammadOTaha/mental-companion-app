package com.omar.mentalcompanion.domain.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.omar.mentalcompanion.domain.services.NotificationService

class ReminderNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        NotificationService.createNotification(
            context,
            inputData.getString("title")!!,
            inputData.getString("content")!!
        )

        return Result.success()
    }

    companion object {
        const val WORKER_TAG = "reminder_notification_work"
        const val WORKER_NAME = "com.omar.mentalcompanion.domain.workers.ReminderNotificationWorker"

        fun createInputData(title: String, content: String) = workDataOf(
            "title" to title,
            "content" to content
        )
    }
}