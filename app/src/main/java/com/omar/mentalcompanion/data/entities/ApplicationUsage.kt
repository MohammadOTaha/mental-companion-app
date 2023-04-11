package com.omar.mentalcompanion.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "applications_usage")
data class ApplicationUsage(
    @PrimaryKey
    @ColumnInfo(name = "app_name")
    var appName: String,
    @ColumnInfo(name = "usage_time_mins")
    var usageTimeMins: Long,
    @ColumnInfo(name = "created_at")
    var createdAt: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
) {
    fun convertAppUsageToMins(): ApplicationUsage {
        return ApplicationUsage(appName, usageTimeMins / 60)
    }

    fun convertToAppNameToSimple(): ApplicationUsage {
        return ApplicationUsage(appName.substringAfterLast("."), usageTimeMins)
    }
}