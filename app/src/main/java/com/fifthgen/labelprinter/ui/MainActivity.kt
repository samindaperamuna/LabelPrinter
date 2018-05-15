package com.fifthgen.labelprinter.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.model.TableItem
import com.fifthgen.labelprinter.ui.adapter.TableAdapter
import com.fifthgen.labelprinter.ui.custom.AdapterTableLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tableLayout = findViewById<AdapterTableLayout>(R.id.tableLayout)
        tableLayout.rows = ROWS
        tableLayout.columns = COLS

        val tableItems = ArrayList<TableItem>()

        val charList = ArrayList<Char>()
        charList.add('#')
        charList.addAll(('A'..'Z').toList())
        charList.add('\u21C5')

        (0 until ROWS * COLS).forEach { i ->
            val tableItem = TableItem(i, charList[i].toString())
            if (tableItem.value == '#'.toString() || tableItem.value == '\u21C5'.toString())
                tableItem.available = true

            tableItems.add(tableItem)
        }

        val tableAdapter = TableAdapter(this, tableItems) as TableAdapter?
        tableLayout.adapter = tableAdapter
    }

    companion object {
        private const val ROWS = 7
        private const val COLS = 4
    }
}
