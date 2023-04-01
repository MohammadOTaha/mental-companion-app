package com.omar.mentalcompanion.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val time: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)