package com.omar.mentalcompanion

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import com.omar.mentalcompanion.data.dto.AppUsage
import com.omar.mentalcompanion.data.dto.PhoneCallsLog
import com.omar.mentalcompanion.data.tracked_data.LocationLiveData
import com.omar.mentalcompanion.data.tracked_data.PhoneCallsLogData
import com.omar.mentalcompanion.data.tracked_data.UsageStatsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    application: Application,
    private val usageStats: UsageStatsData,
    private val location: LocationLiveData
) : AndroidViewModel(application) {

    private val _appUsageList = MutableStateFlow(usageStats.getAppUsages())
    private val _totalScreenTime = MutableStateFlow(usageStats.getTotalScreenTime())

    private val _phoneCallsLog = MutableStateFlow(PhoneCallsLogData(application).getCallLogs())

    val locationLiveData: LocationLiveData
        get() = location

    val appUsageList: MutableStateFlow<List<AppUsage>>
        get() = _appUsageList

    val totalScreenTime: MutableStateFlow<String>
        get() = _totalScreenTime

    val phoneCallsLog: MutableStateFlow<List<PhoneCallsLog>>
        get() = _phoneCallsLog

    init {
        location.startLocationUpdates()
    }

    fun updateLocation() {
        location.startLocationUpdates()
    }

    fun updateAppUsageList() {
        _appUsageList.value = usageStats.getAppUsages()
        _totalScreenTime.value = usageStats.getTotalScreenTime()
    }

    fun updateTotalScreenTime() {
        _totalScreenTime.value = usageStats.getTotalScreenTime()
    }

    override fun onCleared() {
        super.onCleared()
        location.stopLocationUpdates()
    }
}