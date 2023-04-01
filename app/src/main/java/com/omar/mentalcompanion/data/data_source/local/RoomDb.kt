package com.omar.mentalcompanion.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omar.mentalcompanion.data.data_source.local.dao.LocationDao
import com.omar.mentalcompanion.data.entities.Location

@Database(entities = [Location::class], version = 1)
abstract class RoomDb : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        const val DATABASE_NAME = "mental_companion_db"
    }
}