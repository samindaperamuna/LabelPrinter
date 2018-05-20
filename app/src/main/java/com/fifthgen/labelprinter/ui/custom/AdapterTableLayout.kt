package com.fifthgen.labelprinter.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.TableLayout
import android.widget.TableRow
import com.fifthgen.labelprinter.ui.adapter.TableAdapter

class AdapterTableLayout(context: Context, attrs: AttributeSet) : TableLayout(context, attrs), ViewTreeObserver.OnGlobalLayoutListener {

    var adapter: TableAdapter? = null
        set(_adapter) {
            field = _adapter
            viewTreeObserver.addOnGlobalLayoutListener(this)
        }

    var columns: Int = 0
    var rows: Int = 0
    var data = ArrayList<String>()

    private fun useAdapter(layoutHeight: Int) {
        if (adapter != null && !adapter!!.isEmpty) {
            for (i in 0 until rows) {
                val row = TableRow(context)
                val layoutParams = TableRow.LayoutParams(LayoutParams.MATCH_PARENT, layoutHeight / rows)

                for (j in 0 until columns) {
                    val index = j + (i * columns)
                    row.addView(adapter?.getView(index, null, this), layoutParams)
                }
                addView(row)
            }
        }
    }

    /**
     * Use the adapter after layout is drawn.
     */
    override fun onGlobalLayout() {
        viewTreeObserver.removeOnGlobalLayoutListener(this)

        useAdapter(measuredHeight)
    }
}