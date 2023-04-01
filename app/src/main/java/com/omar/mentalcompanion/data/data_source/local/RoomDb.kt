package com.omar.mentalcompanion.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omar.mentalcompanion.data.data_source.local.dao.ApplicationUsageDao
import com.omar.mentalcompanion.data.data_source.local.dao.LocationDao
import com.omar.mentalcompanion.data.entities.ApplicationUsage
import com.omar.mentalcompanion.data.entities.Location

@Database(entities = [Location::class, ApplicationUsage::class], version = 2)
abstract class RoomDb : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun applicationUsageDao(): ApplicationUsageDao

    companion object {
        const val DATABASE_NAME = "mental_companion_db"
    }
}