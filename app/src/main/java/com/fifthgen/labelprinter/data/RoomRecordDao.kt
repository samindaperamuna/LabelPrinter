package com.fifthgen.labelprinter.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.fifthgen.labelprinter.data.model.RoomRecord
import java.util.*

@Dao
interface RoomRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RoomRecord)

    @Query("DELETE FROM room_records")
    fun deleteAll()

    @Query("DELETE FROM room_records WHERE record_date=:date")
    fun deleteDaysRecords(date: Date)

    @Query("SELECT * FROM  room_records WHERE record_date=:date ORDER BY id Asc")
    fun getRecords(date: Date): List<RoomRecord>
}