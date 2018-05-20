package com.fifthgen.labelprinter.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "room_records")
class RoomRecord(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "room_number") val roomNumber: Int,
        @ColumnInfo(name = "group_name") val groupName: String,
        @ColumnInfo(name = "customer_name") val customerName: String,
        @ColumnInfo(name = "start_date") val startDate: Date,
        @ColumnInfo(name = "end_date") val endDate: Date,
        @ColumnInfo(name = "record_id") val recordId: Int,
        @ColumnInfo(name = "record_date") val recordDate: Date
)