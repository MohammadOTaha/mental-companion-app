package com.omar.mentalcompanion.services

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build

data class AppUsage(val packageName: String, val usageTime: Long) : Comparable<AppUsage> {
    override fun toString(): String {
        val hours = usageTime / 3600
        val minutes = (usageTime % 3600) / 60
        val seconds = usageTime % 60

        return "$packageName: $hours hours, $minutes minutes, $seconds seconds"
    }

    override fun compareTo(other: AppUsage): Int {
        return other.usageTime.compareTo(this.usageTime)
    }
}

class UsageStatsService(context: Context) {
    private val usageStatsManager: UsageStatsManager

    init {
        usageStatsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        } else {
            context.getSystemService("usagestats") as UsageStatsManager
        }

        if (usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                System.currentTimeMillis() - ONE_DAY_MILLIS,
                System.currentTimeMillis()
            ).isEmpty()
        ) {
            context.startActivity(Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS))
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

    fun refresh() {
        val time = System.currentTimeMillis()
        usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - ONE_DAY_MILLIS,
            time
        )
    }

    fun isUsageStatsPermissionGranted(): Boolean {
        return getAppUsages().isNotEmpty()
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