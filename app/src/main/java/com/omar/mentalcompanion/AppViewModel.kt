package com.omar.mentalcompanion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.omar.mentalcompanion.data.dto.AppUsage
import com.omar.mentalcompanion.data.dto.PhoneCallsLog
import com.omar.mentalcompanion.domain.tracked_data.PhoneCallsLogData
import com.omar.mentalcompanion.domain.tracked_data.UsageStatsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    application: Application,
    private val usageStats: UsageStatsData,
) : AndroidViewModel(application) {


}