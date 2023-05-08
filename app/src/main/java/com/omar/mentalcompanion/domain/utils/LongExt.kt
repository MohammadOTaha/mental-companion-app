package com.omar.mentalcompanion.domain.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedTimeString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return dateFormat.format(this)
}