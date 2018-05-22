package com.fifthgen.labelprinter.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fifthgen.labelprinter.data.RoomRecordParser
import com.fifthgen.labelprinter.data.RoomRecordRepository
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.network.WebClient
import com.fifthgen.labelprinter.util.Constants.Companion.BROADCAST_FAIL_ACTION
import com.fifthgen.labelprinter.util.Constants.Companion.BROADCAST_SUCCESS_ACTION
import com.fifthgen.labelprinter.util.Constants.Companion.DATE_PATTERN
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_DATE
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_ERROR
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_OFFLINE
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_RECORDS
import com.fifthgen.labelprinter.util.Constants.Companion.REMOTE_URL
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.HttpStatus
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.reflect.KClass

class FetchRoomRecordsService : IntentService(KClass::class.qualifiedName) {

    override fun onHandleIntent(intent: Intent) {
        val offline = intent.extras.getBoolean(PARAM_OFFLINE)
        val date = intent.extras.getString(PARAM_DATE)

        if (!offline) {
            Handler(Looper.getMainLooper()).post({ fetchRemote(date) })
        } else {
            fetchLocal(date)
        }
    }

    /**
     * Fetch data from the remote server.
     */
    private fun fetchRemote(date: String) {
        val requestParams = RequestParams()
        requestParams.add(PARAM_DATE, date)

        WebClient.httpGet(REMOTE_URL, requestParams, object : TextHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                if (statusCode == HttpStatus.SC_OK) {
                    val records = RoomRecordParser.parseRecords(responseString as String, date)

                    // Persist records.
                    val executorService = Executors.newSingleThreadExecutor()
                    executorService.submit { saveToDb(records) }

                    val broadcastIntent = Intent(BROADCAST_SUCCESS_ACTION)
                    broadcastIntent.putExtra(PARAM_RECORDS, records)
                    sendBroadcast(broadcastIntent)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                val broadcastIntent = Intent(BROADCAST_FAIL_ACTION)
                broadcastIntent.putExtra(PARAM_ERROR, "Cannot connect to the remote url. Try using offline mode.")
                sendBroadcast(broadcastIntent)
            }
        })
    }

    /**
     * Fetch date from the local database.
     */
    private fun fetchLocal(date: String) {
        val sdt = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        val broadcastIntent = Intent()

        try {
            val repository = RoomRecordRepository(application)
            val records = repository.getRecords(sdt.parse(date))
            broadcastIntent.action = BROADCAST_SUCCESS_ACTION
            broadcastIntent.putExtra(PARAM_RECORDS, records as ArrayList)
        } catch (e: Exception) {
            broadcastIntent.action = BROADCAST_FAIL_ACTION
            broadcastIntent.putExtra(PARAM_ERROR, "Couldn't persist data. Error: ${e.localizedMessage}")
        }

        sendBroadcast(broadcastIntent)
    }

    /**
     * Cache the records to the database.
     */
    fun saveToDb(records: List<RoomRecord>) {
        val repository = RoomRecordRepository(application)

        // Delete existing records for the date.
        repository.deleteDaysRecords(records[0].recordDate)

        // Insert the records.
        records.forEach { repository.insert(it) }

        Log.i(this::class.qualifiedName, "${records.size} records saved.")
    }
}