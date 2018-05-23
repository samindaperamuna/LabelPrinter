package com.fifthgen.labelprinter.ui.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.Session
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.model.TableItem
import com.fifthgen.labelprinter.service.FetchRoomRecordsService
import com.fifthgen.labelprinter.ui.DirectoryActivity
import com.fifthgen.labelprinter.ui.SettingsActivity
import com.fifthgen.labelprinter.ui.custom.AutoResizeTextView
import com.fifthgen.labelprinter.util.Constants
import com.fifthgen.labelprinter.util.Constants.Companion.ALPHABET
import com.fifthgen.labelprinter.util.Constants.Companion.COLS
import com.fifthgen.labelprinter.util.Constants.Companion.DATE_PATTERN
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_DATE
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_INDEX
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_RECORDS
import com.fifthgen.labelprinter.util.Constants.Companion.ROWS
import java.text.SimpleDateFormat
import java.util.*

class TableAdapter(context: Context?, itemCollection: List<TableItem>, private val records: List<RoomRecord>) :
        ArrayAdapter<TableItem>(context, 0, itemCollection), View.OnClickListener {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        // Get the data from the position.
        val tableItem = getItem(position)
        var view = convertView

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null)
            view = LayoutInflater.from(context).inflate(R.layout.table_item, parent, false)

        val itemView = ItemView(view)

        // Lookup view for data population.
        itemView.textView.text = tableItem.value
        if (tableItem.available) {
            itemView.textView.setTextColor(ContextCompat.getColor(context, R.color.colorHighlight))
            itemView.textView.isClickable = true
            itemView.textView.id = tableItem.id
            itemView.textView.setOnClickListener(this)
        }

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            0 -> {
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
            }
            (ROWS * COLS) - 1 -> {
                val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
                val sharedPref = context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
                val prevDate = sharedPref.getString(PARAM_DATE, sdf.format(Date()))

                val calendar = Calendar.getInstance()
                calendar.time = sdf.parse(prevDate)
                val d = calendar.get(Calendar.DAY_OF_MONTH)
                val m = calendar.get(Calendar.MONTH)
                val y = calendar.get(Calendar.YEAR)

                DatePickerDialog(context, R.style.AppTheme_Dialog,
                        DatePickerDialog.OnDateSetListener { _, year, month, day ->
                            val date = "$day/${month + 1}/$year"
                            val session = context.applicationContext as Session

                            // Save prevDate.
                            sharedPref.edit().putString(PARAM_DATE, date).apply()

                            // Start the data fetch service.
                            val fetchDataIntent = Intent(context, FetchRoomRecordsService::class.java)
                            fetchDataIntent.putExtra(Constants.PARAM_OFFLINE, session.offline)
                            fetchDataIntent.putExtra(Constants.PARAM_DATE, date)
                            context.startService(fetchDataIntent)
                        }, y, m, d).show()
            }
            else -> {
                val index = ALPHABET[view.id.minus(1)]
                val filteredRecords = records.filter { it.customerName[0] == index } as ArrayList

                val intent = Intent(context, DirectoryActivity::class.java)
                intent.putExtra(PARAM_INDEX, index.toString())
                intent.putExtra(PARAM_RECORDS, filteredRecords)
                context.startActivity(intent)
            }
        }
    }

    private class ItemView(view: View?) {
        var textView: AutoResizeTextView = view?.findViewById(R.id.textView) as AutoResizeTextView
    }
}