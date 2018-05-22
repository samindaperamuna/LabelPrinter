package com.fifthgen.labelprinter.data

import android.app.Application
import android.os.AsyncTask
import com.fifthgen.labelprinter.data.model.RoomRecord
import java.util.*

class RoomRecordRepository(application: Application) {

    private val db = AppDatabase.getDatabase(application)
    private val roomRecordDao = db.roomRecordDao()

    fun insert(record: RoomRecord) {
        InsertAsyncClass(roomRecordDao).execute(record)
    }

    fun deleteDaysRecords(date: Date) {
        roomRecordDao.deleteDaysRecords(date)
    }

    fun getRecords(date: Date): List<RoomRecord> {
        return roomRecordDao.getRecords(date)
    }

    private class InsertAsyncClass(private val asyncTaskDao: RoomRecordDao) : AsyncTask<RoomRecord, Unit, Unit>() {

        override fun doInBackground(vararg params: RoomRecord) {
            asyncTaskDao.insert(params[0])
        }
    }
}