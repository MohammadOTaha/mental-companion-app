package com.omar.mentalcompanion.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.omar.mentalcompanion.data.entities.MetaData

@Dao
interface MetaDataDao {

    @Upsert
    suspend fun upsertMetaData(metaData: MetaData)

    @Query("SELECT value FROM metadata WHERE key = :key")
    suspend fun getMetaData(key: String): String?

    @Query("SELECT * FROM metadata")
    suspend fun getAllMetaData(): List<MetaData>
}