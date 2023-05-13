package com.omar.mentalcompanion.domain.tracked_data

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import com.omar.mentalcompanion.data.dto.PhoneCallsLog
import com.omar.mentalcompanion.domain.utils.hasPhoneCallsLogPermission
import com.omar.mentalcompanion.domain.utils.toFormattedTimeString

class PhoneCallsLogData(private val context: Context) {
    private val callLogs = mutableListOf<PhoneCallsLog>()

    @SuppressLint("Range")
    fun getCallLogs(): List<PhoneCallsLog> {
        if (!context.hasPhoneCallsLogPermission()) {
            return callLogs
        }

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            CallLog.Calls.DATE + " >= ?",
            arrayOf((System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 14).toString()),
            CallLog.Calls.DATE + " DESC"
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
                val date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))
                val duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))

                val callType = when (type) {
                    CallLog.Calls.OUTGOING_TYPE.toString() -> "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE.toString() -> "INCOMING"
                    CallLog.Calls.MISSED_TYPE.toString() -> "MISSED"
                    else -> "UNKNOWN"
                }

                val callLog = PhoneCallsLog(callType, date.toLong().toFormattedTimeString(), duration)
                callLogs.add(callLog)
            }
            cursor.close()
        }

        return callLogs
    }
}