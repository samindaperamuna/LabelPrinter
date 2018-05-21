package com.fifthgen.labelprinter.service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.util.Constants.Companion.BROADCAST_FAIL_ACTION
import com.fifthgen.labelprinter.util.Constants.Companion.BROADCAST_SUCCESS_ACTION
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_ERROR
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_RECORDS

class FetchRoomRecordsBroadcastReceiver(private val broadcastListener: FetchRoomRecordsBroadcastListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var records = ArrayList<RoomRecord>()
        if (intent.action == BROADCAST_SUCCESS_ACTION) {
            val data = intent.extras.getSerializable(PARAM_RECORDS) as List<*>
            records = data.filterIsInstance<RoomRecord>() as ArrayList
            broadcastListener.onBroadcastReceived(true, records, "")
        } else if (intent.action == BROADCAST_FAIL_ACTION) {
            val message = intent.extras.getSerializable(PARAM_ERROR) as String
            broadcastListener.onBroadcastReceived(false, records, message)
        }
    }

    interface FetchRoomRecordsBroadcastListener {
        fun onBroadcastReceived(success: Boolean, data: List<RoomRecord>, message: String)
    }
}