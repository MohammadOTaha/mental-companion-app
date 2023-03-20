package com.omar.mentalcompanion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.omar.mentalcompanion.data.tracked_data.LocationLiveData
import com.omar.mentalcompanion.data.tracked_data.UsageStatsData

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _locationLiveData = LocationLiveData(application)
    private val _usageStatsData = UsageStatsData(application)

    fun getLocationLiveData() = _locationLiveData

    fun startLocationUpdates() {
        _locationLiveData.startLocationUpdates()
    }

    fun getUsageStatsData() = _usageStatsData.getAppUsages()

    fun getTotalScreenTime() = _usageStatsData.getTotalScreenTime()

    fun updateUsageStatsData() {
        _usageStatsData.refresh()
    }

}