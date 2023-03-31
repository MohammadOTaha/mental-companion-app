package com.omar.mentalcompanion

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import com.omar.mentalcompanion.data.dto.AppUsage
import com.omar.mentalcompanion.data.dto.PhoneCallsLog
import com.omar.mentalcompanion.data.tracked_data.LocationLiveData
import com.omar.mentalcompanion.data.tracked_data.PhoneCallsLogData
import com.omar.mentalcompanion.data.tracked_data.UsageStatsData
import kotlinx.coroutines.flow.MutableStateFlow

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _locationLiveData = LocationLiveData(application)

    private val _usageStatsData: UsageStatsData by lazy { UsageStatsData(application) }
    private val _appUsageList = MutableStateFlow(_usageStatsData.getAppUsages())
    private val _totalScreenTime = MutableStateFlow(_usageStatsData.getTotalScreenTime())

    private val _phoneCallsLog = MutableStateFlow(PhoneCallsLogData(application).getCallLogs())

    val locationLiveData: LocationLiveData
        get() = _locationLiveData

    val appUsageList: MutableStateFlow<List<AppUsage>>
        get() = _appUsageList

    val totalScreenTime: MutableStateFlow<String>
        get() = _totalScreenTime

    val phoneCallsLog: MutableStateFlow<List<PhoneCallsLog>>
        get() = _phoneCallsLog

    init {
        _locationLiveData.startLocationUpdates()
    }

    fun updateLocation() {
        _locationLiveData.startLocationUpdates()
    }

    fun updateAppUsageList() {
        _appUsageList.value = _usageStatsData.getAppUsages()
        _totalScreenTime.value = _usageStatsData.getTotalScreenTime()
    }

    fun updateTotalScreenTime() {
        _totalScreenTime.value = _usageStatsData.getTotalScreenTime()
    }

    override fun onCleared() {
        super.onCleared()
        _locationLiveData.stopLocationUpdates()
    }
}