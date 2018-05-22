package com.fifthgen.labelprinter.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.data.model.RoomRecord

class IndexAdapter(private val records: List<RoomRecord>) : RecyclerView.Adapter<IndexAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.index_item, parent, false) as LinearLayout
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: IndexAdapter.ViewHolder, position: Int) {
        holder.layout.findViewById<TextView>(R.id.itemTextView).text = records[position].customerName
    }

    override fun getItemCount() = records.size

    class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)
}