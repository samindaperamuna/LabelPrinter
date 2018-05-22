package com.fifthgen.labelprinter.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.data.model.RoomRecord
import com.fifthgen.labelprinter.ui.adapter.IndexAdapter
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_INDEX
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_RECORDS

class DirectoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)

        val index = intent.getStringExtra(PARAM_INDEX)
        val data = intent.getSerializableExtra(PARAM_RECORDS) as List<*>
        val records = data.filterIsInstance<RoomRecord>()

        val indexTextView = findViewById<TextView>(R.id.indexTextView)
        indexTextView.text = index

        findViewById<RecyclerView>(R.id.indexItemsView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = IndexAdapter(records)
        }
    }
}
