package com.fifthgen.labelprinter.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.Session
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.service.FetchRoomRecordsService
import com.fifthgen.labelprinter.service.receiver.FetchRoomRecordsBroadcastReceiver
import com.fifthgen.labelprinter.service.receiver.FetchRoomRecordsBroadcastReceiver.FetchRoomRecordsBroadcastListener
import com.fifthgen.labelprinter.util.Constants
import com.fifthgen.labelprinter.util.Constants.Companion.APP_PREFERENCES
import com.fifthgen.labelprinter.util.Constants.Companion.BROADCAST_SUCCESS_ACTION
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_DATE
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_OFFLINE
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_RECORDS
import com.fifthgen.labelprinter.util.InternetCheck
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : AppCompatActivity(), InternetCheck.InternetCheckListener, FetchRoomRecordsBroadcastListener {

    private val roomRecordsBroadcastReceiver = FetchRoomRecordsBroadcastReceiver(this)
    private var date = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Get last used date from shared preferences if available, else use today's date.
        val sharedPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val sdf = SimpleDateFormat(Constants.DATE_PATTERN, Locale.getDefault())
        date = sharedPref.getString(PARAM_DATE, sdf.format(Date()))

        InternetCheck(this).isInternetConnectionAvailable(this)
    }

    override fun onResume() {
        super.onResume()

        // Register broadcast receiver.
        val filter = IntentFilter(BROADCAST_SUCCESS_ACTION)
        registerReceiver(roomRecordsBroadcastReceiver, filter)
    }

    override fun onPause() {
        // Unregister the broadcast receiver.
        unregisterReceiver(roomRecordsBroadcastReceiver)

        super.onPause()
    }

    /**
     * Handles internet connection check results.
     *
     * If there is an internet connection, call the <code>FetchRoomRecords</code> service and wait
     * for the results. When data is received, save it in the database and pass it to <code>MainActivity</code>.
     *
     * If there is no internet connection,
     * ask user whether he/she wants to continue in offline mode. If user selects the offline mode
     * save the choice in configuration files to be accessed in settings. If the user selects not to
     * work in offline mode, terminate the activity.
     */
    override fun onComplete(connected: Boolean) {
        if (connected) {
            val fetchOnlineIntent = Intent(this, FetchRoomRecordsService::class.java)
            fetchOnlineIntent.putExtra(PARAM_OFFLINE, false)
            fetchOnlineIntent.putExtra(PARAM_DATE, date)
            startService(fetchOnlineIntent)
        } else {
            AlertDialog.Builder(this@SplashActivity, R.style.AppTheme_Dialog)
                    .setTitle("No internet connection available.")
                    .setMessage("Do you want to start in offline mode?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Set session to offline.
                        val session = application as Session
                        session.offline = true

                        val fetchOfflineIntent = Intent(this, FetchRoomRecordsService::class.java)
                        fetchOfflineIntent.putExtra(PARAM_OFFLINE, session.offline)
                        fetchOfflineIntent.putExtra(PARAM_DATE, date)
                        startService(fetchOfflineIntent)
                    }
                    .setNegativeButton("No") { _, _ -> this@SplashActivity.finish() }
                    .show()
        }
    }

    override fun onBroadcastReceived(success: Boolean, data: List<RoomRecord>, message: String) {
        var records = ArrayList<RoomRecord>()

        if (success) {
            records = data as ArrayList<RoomRecord>
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        startMainActivity(records)
    }

    /**
     * Start the main activity by loading the data for the current day if available or else
     * an empty array.
     */
    private fun startMainActivity(records: List<RoomRecord>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(PARAM_RECORDS, records as ArrayList)
        startActivity(intent)
    }
}
