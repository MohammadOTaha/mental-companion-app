package com.omar.mentalcompanion.domain.repositories

import com.omar.mentalcompanion.data.data_source.local.dao.LocationDao
import com.omar.mentalcompanion.data.entities.Location

class LocationRepository(
    private val locationDao: LocationDao
) {

    fun getAllLocations(): List<Location> {
        return locationDao.getAllLocations()
    }

    suspend fun insertLocation(location: Location) {
        locationDao.insertLocation(location)
    }
}