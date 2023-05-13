package com.omar.mentalcompanion.domain.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.data.data_source.local.dao.MetaDataDao
import com.omar.mentalcompanion.data.entities.MetaData
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.domain.utils.toFormattedDateString

class MetaDataRepository(
    private val metaDataDao: MetaDataDao
) {
    private val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun upsertMetaData(metaData: MetaData) {
        metaDataDao.upsertMetaData(metaData)
        metaDataDao.upsertMetaData(metaData.copy(key = "${metaData.key}@" + getDate()))

        if (metaData.key == MetaDataKeys.LAST_QUESTIONNAIRE_SCORE) {
            exportPhqScoreToFirestore()
        }

        if (metaData.key == MetaDataKeys.LAST_SLEEP_HOURS) {
            exportSleepHoursToFirestore()
        }

        exportMetaDataToFirestore()
    }

    suspend fun getMetaDataValue(key: String): String? = metaDataDao.getMetaData(key)

    private suspend fun exportMetaDataToFirestore() {
        val uuid = metaDataDao.getMetaData(MetaDataKeys.UUID)

        if (uuid != null) {
            firestoreDb.collection("meta_data")
                .document(uuid)
                .set(metaDataDao.getAllMetaData().associateBy({ it.key }, { it.value }))
        }
    }

    private suspend fun exportPhqScoreToFirestore() {
        val uuid = metaDataDao.getMetaData(MetaDataKeys.UUID)

        val allMetaData = metaDataDao.getAllMetaData()
        val phqScores = mutableListOf<Map<String, String>>()

        for (metaData in allMetaData) {
            if (metaData.key.contains(MetaDataKeys.LAST_QUESTIONNAIRE_SCORE)) {
                phqScores.add(mapOf(metaData.key to metaData.value))
            }
        }

        if (uuid != null) {
            firestoreDb.collection("phq_scores")
                .document(uuid)
                .set(phqScores.associateBy({ it.keys.first() }, { it.values.first() }))
        }
    }

    private suspend fun exportSleepHoursToFirestore() {
        val uuid = metaDataDao.getMetaData(MetaDataKeys.UUID)

        val allMetaData = metaDataDao.getAllMetaData()
        val sleepHours = mutableListOf<Map<String, String>>()

        for (metaData in allMetaData) {
            if (metaData.key.contains(MetaDataKeys.LAST_SLEEP_HOURS)) {
                sleepHours.add(mapOf(metaData.key to metaData.value))
            }
        }

        if (uuid != null) {
            firestoreDb.collection("sleep_hours")
                .document(uuid)
                .set(sleepHours.associateBy({ it.keys.first() }, { it.values.first() }))
        }
    }


    private fun getDate(): String {
        return System.currentTimeMillis().toFormattedDateString()
    }
}