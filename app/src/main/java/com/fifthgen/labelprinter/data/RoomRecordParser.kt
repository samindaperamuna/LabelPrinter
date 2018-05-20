package com.fifthgen.labelprinter.data

import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.util.Constants.Companion.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class RoomRecordParser {

    companion object {

        private const val LINE_SEPARATOR = "~"
        private const val COL_SEPARATOR = "+"
        private const val VALID_COL_COUNT = 9

        fun parseRecords(records: String, date: String): ArrayList<RoomRecord> {
            val parsedRecords = ArrayList<RoomRecord>()
            val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

            records.split(LINE_SEPARATOR).forEach {
                it.split(COL_SEPARATOR).forEach {
                    if (it.count() == VALID_COL_COUNT) {
                        val record = RoomRecord(
                                0,
                                it[0].toInt(),
                                it[1].toString(),
                                it[2].toString(),
                                sdf.parse(it[3].toString()),
                                sdf.parse(it[4].toString()),
                                it[6].toInt(),
                                sdf.parse(date)
                        )

                        parsedRecords.add(record)
                    }
                }
            }

            return parsedRecords
        }
    }
}