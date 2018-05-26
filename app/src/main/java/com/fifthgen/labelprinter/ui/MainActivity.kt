package com.fifthgen.labelprinter.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.model.TableItem
import com.fifthgen.labelprinter.service.receiver.FetchRoomRecordsBroadcastReceiver
import com.fifthgen.labelprinter.ui.adapter.TableAdapter
import com.fifthgen.labelprinter.ui.custom.AdapterTableLayout
import com.fifthgen.labelprinter.util.Constants
import com.fifthgen.labelprinter.util.Constants.Companion.ALPHABET
import com.fifthgen.labelprinter.util.Constants.Companion.COLS
import com.fifthgen.labelprinter.util.Constants.Companion.NET_SYMBOL
import com.fifthgen.labelprinter.util.Constants.Companion.POUND_SYMBOL
import com.fifthgen.labelprinter.util.Constants.Companion.ROWS

class MainActivity : AppCompatActivity(), FetchRoomRecordsBroadcastReceiver.FetchRoomRecordsBroadcastListener {

    private val roomRecordsBroadcastReceiver = FetchRoomRecordsBroadcastReceiver(this)

    private val tableLayout: AdapterTableLayout
        get() {
            val tableLayout = findViewById<AdapterTableLayout>(R.id.tableLayout)
            tableLayout.rows = ROWS
            tableLayout.columns = COLS
            return tableLayout
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = intent.extras.getSerializable(Constants.PARAM_RECORDS) as List<*>
        val records = data.filterIsInstance<RoomRecord>() as ArrayList<RoomRecord>
        initTable(records)
    }

    /**
     * Initialise the table with proper letters highlighted.
     */
    private fun initTable(records: List<RoomRecord>) {
        val tableItems = ArrayList<TableItem>()

        val charList = ArrayList<Char>()
        charList.add(Constants.POUND_SYMBOL)
        charList.addAll(ALPHABET.values)
        charList.add(Constants.NET_SYMBOL)

        (0 until ROWS * COLS).forEach table@{ i ->
            val tableItem = TableItem(i, charList[i].toString())
            if (tableItem.value == POUND_SYMBOL.toString() || tableItem.value == NET_SYMBOL.toString())
                tableItem.available = true
            else {
                records.forEach {
                    if (it.customerName[0].toString() == tableItem.value) {
                        tableItem.available = true
                        tableItems.add(tableItem)
                        return@table
                    }
                }
            }

            tableItems.add(tableItem)
        }

        val tableAdapter = TableAdapter(this, tableItems, records) as TableAdapter?
        tableLayout.adapter = tableAdapter
    }

    override fun onPause() {
        // Unregister the broadcast receiver.
        unregisterReceiver(roomRecordsBroadcastReceiver)

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        // Register broadcast receiver.
        val filter = IntentFilter()
        filter.addAction(Constants.BROADCAST_SUCCESS_ACTION)
        filter.addAction(Constants.BROADCAST_FAIL_ACTION)
        registerReceiver(roomRecordsBroadcastReceiver, filter)
    }

    override fun onBroadcastReceived(success: Boolean, data: List<RoomRecord>, message: String) {
        var records = ArrayList<RoomRecord>()
        if (success) {
            records = data as ArrayList<RoomRecord>
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.PARAM_RECORDS, records)
        startActivity(intent)
        finish()
    }
}
