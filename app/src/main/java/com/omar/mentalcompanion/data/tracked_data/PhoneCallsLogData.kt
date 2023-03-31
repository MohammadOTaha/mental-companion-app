package com.omar.mentalcompanion.data.tracked_data

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import com.omar.mentalcompanion.data.dto.PhoneCallsLog

class PhoneCallsLogData(private val context: Context) {
    private val callLogs = mutableListOf<PhoneCallsLog>()

    @SuppressLint("Range")
    fun getCallLogs(): List<PhoneCallsLog> {
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
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

                val callLog = PhoneCallsLog(callType, date, duration)
                callLogs.add(callLog)
            }
            cursor.close()
        }

        return callLogs
    }
}