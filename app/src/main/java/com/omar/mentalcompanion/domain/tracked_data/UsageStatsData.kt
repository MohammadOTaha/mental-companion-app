package com.omar.mentalcompanion.domain.tracked_data

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.omar.mentalcompanion.data.dto.AppUsage

class UsageStatsData(context: Context) {
    private val usageStatsManager: UsageStatsManager

    init {
        usageStatsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        } else {
            context.getSystemService("usagestats") as UsageStatsManager
        }
    }

    fun getTotalScreenTime(): String {
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - ONE_DAY_MILLIS,
            time
        )

        var totalScreenTime: Long = 0
        for (stat in stats) {
            if (stat.totalTimeInForeground > 0) {
                totalScreenTime += stat.totalTimeInForeground / 1000
            }
        }

        return totalScreenTime.toFormattedTime()
    }

    fun getAppUsages(): List<AppUsage> {
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - ONE_DAY_MILLIS,
            time
        )

        val appUsages = mutableListOf<AppUsage>()
        for (stat in stats) {
            if (stat.totalTimeInForeground > 0) {
                appUsages.add(AppUsage(stat.packageName, stat.totalTimeInForeground / 1000))
            }
        }

        return appUsages.sorted()
    }

    companion object {
        private const val ONE_DAY_MILLIS = 1000 * 60 * 60 * 24

        private fun Long.toFormattedTime(): String {
            val hours = this / 3600
            val minutes = (this % 3600) / 60
            val seconds = this % 60

            return "$hours hours, $minutes minutes, $seconds seconds"
        }
    }
}