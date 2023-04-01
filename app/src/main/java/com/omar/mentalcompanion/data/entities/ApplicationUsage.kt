package com.omar.mentalcompanion.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications_usage")
data class ApplicationUsage(
    @PrimaryKey
    @ColumnInfo(name = "app_name")
    var appName: String,
    @ColumnInfo(name = "usage_time_mins")
    var usageTimeMins: Long
) {
    fun convertAppUsageToMins(): ApplicationUsage {
        return ApplicationUsage(appName, usageTimeMins / 60)
    }

    fun convertToAppNameToSimple(): ApplicationUsage {
        return ApplicationUsage(appName.substringAfterLast("."), usageTimeMins)
    }
}