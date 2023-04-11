package com.omar.mentalcompanion.data.services

import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.domain.repositories.ApplicationUsageRepository
import javax.inject.Inject

class SyncService @Inject constructor(
    private val applicationUsageRepository: ApplicationUsageRepository,
) {
    private val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun execute() {
        val applicationUsage = applicationUsageRepository.getTodayApplicationsUsage()

        applicationUsage.collect { appUsage ->
            appUsage.forEach { app ->
                firestoreDb.collection("applications_usage")
                    .document(app.appName)
                    .set(app)
            }
        }
    }
}