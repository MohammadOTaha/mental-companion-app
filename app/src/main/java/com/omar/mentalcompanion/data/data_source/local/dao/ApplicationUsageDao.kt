package com.omar.mentalcompanion.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.omar.mentalcompanion.data.entities.ApplicationUsage
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationUsageDao {

    @Upsert
    suspend fun upsertApplicationUsage(applicationUsage: ApplicationUsage)

    @Query("SELECT * FROM applications_usage")
    fun getAllApplicationsUsage(): Flow<List<ApplicationUsage>>

    @Query("SELECT * FROM applications_usage WHERE created_at >= date('now', 'start of day')")
    fun getTodayApplicationsUsage(): Flow<List<ApplicationUsage>>
}