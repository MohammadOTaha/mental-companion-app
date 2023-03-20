package com.omar.mentalcompanion.data.dto

data class AppUsage(var packageName: String, var usageTime: Long) : Comparable<AppUsage> {
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