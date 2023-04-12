package com.omar.mentalcompanion.domain.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.omar.mentalcompanion.data.services.SyncService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncService: SyncService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        syncService.execute()

        return Result.success()
    }
}