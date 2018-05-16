package com.fifthgen.labelprinter.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.model.TableItem
import com.fifthgen.labelprinter.ui.adapter.TableAdapter
import com.fifthgen.labelprinter.ui.custom.AdapterTableLayout
import com.fifthgen.labelprinter.util.Constants
import com.fifthgen.labelprinter.util.Constants.Companion.ALPHABET
import com.fifthgen.labelprinter.util.Constants.Companion.COLS
import com.fifthgen.labelprinter.util.Constants.Companion.NET_SYMBOL
import com.fifthgen.labelprinter.util.Constants.Companion.POUND_SYMBOL
import com.fifthgen.labelprinter.util.Constants.Companion.ROWS

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tableLayout = findViewById<AdapterTableLayout>(R.id.tableLayout)
        tableLayout.rows = ROWS
        tableLayout.columns = COLS

        val tableItems = ArrayList<TableItem>()

        val charList = ArrayList<Char>()
        charList.add(Constants.POUND_SYMBOL)
        charList.addAll(ALPHABET.values)
        charList.add(Constants.NET_SYMBOL)

        (0 until ROWS * COLS).forEach { i ->
            val tableItem = TableItem(i, charList[i].toString())
            if (tableItem.value == POUND_SYMBOL.toString() || tableItem.value == NET_SYMBOL.toString())
                tableItem.available = true

            tableItems.add(tableItem)
        }

        val tableAdapter = TableAdapter(this, tableItems) as TableAdapter?
        tableLayout.adapter = tableAdapter
    }
}
