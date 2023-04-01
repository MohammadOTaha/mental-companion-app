package com.omar.mentalcompanion.domain.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedTimeString(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(this)
}