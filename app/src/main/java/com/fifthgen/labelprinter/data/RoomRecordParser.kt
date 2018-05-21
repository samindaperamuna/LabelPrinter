package com.fifthgen.labelprinter.data

import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.util.Constants.Companion.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

object RoomRecordParser {

    private const val LINE_SEPARATOR = "~"
    private const val COL_SEPARATOR = "+"
    private const val VALID_COL_COUNT = 9

    fun parseRecords(records: String, date: String): ArrayList<RoomRecord> {
        val parsedRecords = ArrayList<RoomRecord>()
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

        records.split(LINE_SEPARATOR).forEach row@{ line ->
            var roomNo: String? = null
            var group: String? = null
            var name: String? = null
            var startDate: Date? = null
            var endDate: Date? = null
            var recordId: Int? = null
            var count = 0

            line.split(COL_SEPARATOR).forEachIndexed { i, s ->
                if (i == 0 && s.isEmpty()) {
                    // Skip record on empty user names.
                    return@row
                } else {
                    when (i) {
                        1 -> roomNo = s
                        2 -> group = s
                        0 -> name = s
                        3 -> startDate = sdf.parse(s)
                        4 -> endDate = sdf.parse(s)
                        6 -> recordId = s.toInt()
                    }
                }

                count++
            }

            // Skip record on invalid column count.
            if (count != VALID_COL_COUNT) return@row

            val record = RoomRecord(0, roomNo!!, group!!, name!!, startDate!!, endDate!!, recordId!!, sdf.parse(date))
            parsedRecords.add(record)
        }

        return parsedRecords
    }
}