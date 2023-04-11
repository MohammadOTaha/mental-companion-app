package com.omar.mentalcompanion.domain.repositories

import com.omar.mentalcompanion.data.data_source.local.dao.ApplicationUsageDao
import com.omar.mentalcompanion.data.entities.ApplicationUsage

class ApplicationUsageRepository(
    private val applicationUsageDao: ApplicationUsageDao
) {

    suspend fun upsertApplicationUsage(applicationUsage: ApplicationUsage) {
        applicationUsageDao.upsertApplicationUsage(
            applicationUsage
                .convertAppUsageToMins()
                .convertToAppNameToSimple()
        )
    }

    fun getAllApplicationsUsage() = applicationUsageDao.getAllApplicationsUsage()

    fun getTodayApplicationsUsage() = applicationUsageDao.getTodayApplicationsUsage()
}