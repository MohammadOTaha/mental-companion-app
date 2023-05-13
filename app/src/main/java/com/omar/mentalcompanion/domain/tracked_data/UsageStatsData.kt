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

    fun getTodayScreenTime(): Long {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - ONE_DAY_MILLIS

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            usageStatsManager.queryAndAggregateUsageStats(startTime, endTime)
                .values
                .sumOf { it.totalTimeInForeground.toInt() / 1000 }
                .toLong()
        } else {
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
                .sumOf { it.totalTimeInForeground.toInt() / 1000 }
                .toLong()
        }
    }

    companion object {
        private const val ONE_DAY_MILLIS = 1000 * 60 * 60 * 24
    }
}