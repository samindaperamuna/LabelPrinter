package com.fifthgen.labelprinter.data.view

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.fifthgen.labelprinter.data.RoomRecordRepository
import com.fifthgen.labelprinter.data.model.RoomRecord
import java.util.*

class RoomRecordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RoomRecordRepository(application)

    fun insert(record: RoomRecord) {
        repository.insert(record)
    }

    fun getRecords(date: Date) {
        repository.getRecords(date)
    }
}