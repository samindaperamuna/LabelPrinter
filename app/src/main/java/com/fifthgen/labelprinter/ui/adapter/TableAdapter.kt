package com.fifthgen.labelprinter.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.model.TableItem
import com.fifthgen.labelprinter.ui.custom.AutoResizeTextView

class TableAdapter(context: Context?, itemCollection: List<TableItem>) :
        ArrayAdapter<TableItem>(context, 0, itemCollection), View.OnClickListener {

    val mItemCollection = itemCollection

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

    override fun onClick(view: View?) {
        when (view?.id) {
            0 -> Toast.makeText(context, "Hello from number key.", Toast.LENGTH_LONG).show()
        }
    }

    private class ItemView(view: View?) {
        var textView: AutoResizeTextView = view?.findViewById(R.id.textView) as AutoResizeTextView
    }
}