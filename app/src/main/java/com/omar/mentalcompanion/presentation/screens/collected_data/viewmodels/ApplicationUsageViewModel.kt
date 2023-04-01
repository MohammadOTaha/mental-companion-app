package com.omar.mentalcompanion.presentation.screens.collected_data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.mentalcompanion.data.entities.ApplicationUsage
import com.omar.mentalcompanion.domain.repositories.ApplicationUsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationUsageViewModel @Inject constructor(
    private val applicationUsageRepository: ApplicationUsageRepository
) : ViewModel() {

    fun upsertApplicationUsage(applicationUsage: ApplicationUsage) {
        viewModelScope.launch(Dispatchers.IO) {
            applicationUsageRepository.upsertApplicationUsage(applicationUsage)
        }
    }


    fun getAllApplicationsUsage() {
        applicationUsageRepository.getAllApplicationsUsage()
    }
}