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

object MetaDataKeys {
    const val UUID = "uuid"
    const val LAST_QUESTIONNAIRE_DATE = "last_questionnaire_date"
    const val LAST_QUESTIONNAIRE_SCORE = "last_questionnaire_score"
    const val INTRODUCTION_COMPLETED = "introduction_completed"
    const val LAST_SLEEP_HOURS = "last_sleep_hours"
    const val LAST_SLEEP_DATE = "last_sleep_date"
}

object MetaDataValues {
    const val EARLIEST_DATE = "1970-01-01"
    const val NO_SCORE = "-1"
    const val TRUE = "1"
    const val FALSE = "0"
}