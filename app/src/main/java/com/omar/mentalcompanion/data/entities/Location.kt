package com.omar.mentalcompanion.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    val latitude: Double,
    val longitude: Double,
    val created_at: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)