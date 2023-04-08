package com.omar.mentalcompanion.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omar.mentalcompanion.data.data_source.local.dao.ApplicationUsageDao
import com.omar.mentalcompanion.data.data_source.local.dao.LocationDao
import com.omar.mentalcompanion.data.data_source.local.dao.MetaDataDao
import com.omar.mentalcompanion.data.entities.ApplicationUsage
import com.omar.mentalcompanion.data.entities.Location
import com.omar.mentalcompanion.data.entities.MetaData

@Database(entities = [Location::class, ApplicationUsage::class, MetaData::class], version = 3)
abstract class RoomDb : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun applicationUsageDao(): ApplicationUsageDao

    abstract fun metaDataDao(): MetaDataDao

    companion object {
        const val DATABASE_NAME = "mental_companion_db"
    }
}