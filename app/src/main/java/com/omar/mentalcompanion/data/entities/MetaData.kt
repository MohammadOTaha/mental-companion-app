package com.omar.mentalcompanion.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metadata")
data class MetaData (
    @PrimaryKey
    var key: String,
    @ColumnInfo(name = "value")
    var value: String
)