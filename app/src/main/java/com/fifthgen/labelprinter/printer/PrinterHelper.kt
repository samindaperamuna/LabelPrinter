package com.fifthgen.labelprinter.printer

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.fifthgen.labelprinter.util.Constants.Companion.IP
import com.fifthgen.labelprinter.util.Constants.Companion.LABEL_NAME_INDEX
import com.fifthgen.labelprinter.util.Constants.Companion.MAC
import java.lang.ref.WeakReference

class PrinterHelper(activity: Activity, private val activityReference: WeakReference<Activity> = WeakReference(activity)) : AsyncResponse {

    fun print(text: Array<String>) {
        val activity = activityReference.get()

        val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)

        val ip = sharedPref.getString(IP, "")
        val mac = sharedPref.getString(MAC, "")
        val labelNameIndex = sharedPref.getInt(LABEL_NAME_INDEX, 0)

        when {
            ip!!.isEmpty() -> Toast.makeText(activity, "Invalid IP address.", Toast.LENGTH_SHORT).show()
            labelNameIndex == 0 -> Toast.makeText(activity, "Invalid label type.", Toast.LENGTH_SHORT).show()
            else -> {
                val printerTask = PrinterTask(this, ip, mac, labelNameIndex)
                printerTask.execute(*text)
            }
        }
    }

    override fun onProcessCompleted(message: String) {
        Toast.makeText(activityReference.get(), message, Toast.LENGTH_LONG).show()
    }
}