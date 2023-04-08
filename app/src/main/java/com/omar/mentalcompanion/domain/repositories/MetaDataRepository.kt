package com.omar.mentalcompanion.domain.repositories

import com.omar.mentalcompanion.data.data_source.local.dao.MetaDataDao
import com.omar.mentalcompanion.data.entities.MetaData

class MetaDataRepository(
    private val metaDataDao: MetaDataDao
) {

    suspend fun upsertMetaData(metaData: MetaData) = metaDataDao.upsertMetaData(metaData)

    suspend fun getMetaData(key: String) = metaDataDao.getMetaData(key)
}