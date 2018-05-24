package com.fifthgen.labelprinter.ui.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.printer.AsyncResponse
import com.fifthgen.labelprinter.printer.TemplatePrintHelper
import com.fifthgen.labelprinter.util.Constants.Companion.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

class IndexAdapter(private val records: List<RoomRecord>, private val context: Context) : RecyclerView.Adapter<IndexAdapter.ViewHolder>(),
        View.OnClickListener, AsyncResponse {

    override fun onClick(view: View) {
        when (view.id) {
            R.id.linearLayout -> {
                AlertDialog.Builder(context, R.style.AppTheme_Dialog)
                        .setTitle("Confirm print label.")
                        .setMessage("Are you sure you want to print this label?")
                        .setPositiveButton("Yes", { _, _ ->
                            val helper = TemplatePrintHelper(context as Activity)
                            val record = records[view.tag as Int]
                            val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

                            helper.print(record.customerName, record.groupName, record.roomNumber,
                                    "${sdf.format(record.startDate)} - ${sdf.format(record.endDate)}",
                                    record.recordId)
                        }).setNegativeButton("No", null).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.index_item, parent, false) as LinearLayout
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: IndexAdapter.ViewHolder, position: Int) {
        val itemTextView = holder.layout.findViewById<TextView>(R.id.itemTextView)
        itemTextView.text = records[position].customerName

        val companyTextView = holder.layout.findViewById<TextView>(R.id.companyTextView)
        companyTextView.text = records[position].groupName

        holder.layout.setOnClickListener(this)
        holder.layout.tag = position
    }

    override fun getItemCount() = records.size

    override fun onProcessCompleted(message: String) {

    }

    class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)
}