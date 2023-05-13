package com.omar.mentalcompanion.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.omar.mentalcompanion.data.entities.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(location: Location)

    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<Location>
}