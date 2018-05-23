package com.fifthgen.labelprinter.ui.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_RECORD

class IndexAdapter(private val records: List<RoomRecord>) : RecyclerView.Adapter<IndexAdapter.ViewHolder>(),
        View.OnClickListener {

    override fun onClick(view: View) {
        when (view.id) {
            R.id.itemTextView -> {
                val intent = Intent()
                intent.putExtra(PARAM_RECORD, records[view.tag as Int])
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
        itemTextView.tag = position
        itemTextView.setOnClickListener(this)
    }

    override fun getItemCount() = records.size

    class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)
}