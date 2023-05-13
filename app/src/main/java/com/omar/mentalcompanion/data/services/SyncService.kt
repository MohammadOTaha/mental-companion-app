package com.omar.mentalcompanion.data.services

import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.domain.repositories.LocationRepository
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.domain.tracked_data.PhoneCallsLogData
import com.omar.mentalcompanion.domain.tracked_data.UsageStatsData
import com.omar.mentalcompanion.domain.utils.toFormattedDateString
import com.omar.mentalcompanion.domain.utils.toFormattedTimeString
import javax.inject.Inject


class SyncService @Inject constructor(
    private val usageStatsData: UsageStatsData,
    private val phoneCallsLogData: PhoneCallsLogData,
    private val metaDataRepository: MetaDataRepository,
    private val locationRepository: LocationRepository
) {
    private val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun execute(isFromWorker: Boolean = true) {
        exportUsageStatsToFirestore(isFromWorker)
        exportLocationToFirestore(isFromWorker)
        exportPhoneCallsToFirestore(isFromWorker)
    }

    private suspend fun exportUsageStatsToFirestore(isFromWorker: Boolean = true) {
        val uuid = metaDataRepository.getMetaDataValue(MetaDataKeys.UUID)
        val todayUsage = usageStatsData.getTodayScreenTime()

        if (uuid != null) {
            firestoreDb.collection("usage_stats" + if (isFromWorker) "_worker" else "")
                .document(uuid)
                .set(mapOf(getTodayDate() to todayUsage))
        }
    }

    private suspend fun exportLocationToFirestore(isFromWorker: Boolean = true) {
        val uuid = metaDataRepository.getMetaDataValue(MetaDataKeys.UUID)
        val allLocations = locationRepository.getAllLocations()

        if (uuid != null) {
            firestoreDb.collection("locations" + if (isFromWorker) "_worker" else "")
                .document(uuid)
                .set(mapOf(getTodayDate() to allLocations))
        }
    }

    private suspend fun exportPhoneCallsToFirestore(isFromWorker: Boolean) {
        val uuid = metaDataRepository.getMetaDataValue(MetaDataKeys.UUID)
        val phoneCalls = phoneCallsLogData.getCallLogs()

        if (uuid != null) {
            firestoreDb.collection("phone_calls" + if (isFromWorker) "_worker" else "")
                .document(uuid)
                .set(mapOf(getTodayDate() to phoneCalls))
        }
    }

    private fun getTodayDate(): String {
        return System.currentTimeMillis().toFormattedDateString()
    }
}