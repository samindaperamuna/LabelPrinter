package com.fifthgen.labelprinter.printer

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.fifthgen.labelprinter.util.Constants
import java.lang.ref.WeakReference

class TemplatePrintHelper(activity: Activity, private val activityReference: WeakReference<Activity> = WeakReference(activity)) : AsyncResponse {

    fun print(name: String, group: String, room: String, dateRange: String, recordNo: Int) {
        val activity = activityReference.get()

        val sharedPref = activity!!.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)

        val ip = sharedPref.getString(Constants.IP, "")
        val mac = sharedPref.getString(Constants.MAC, "")
        val labelNameIndex = sharedPref.getInt(Constants.LABEL_NAME_INDEX, 0)

        when {
            ip!!.isEmpty() -> Toast.makeText(activity, "Invalid IP address.", Toast.LENGTH_SHORT).show()
            labelNameIndex == 0 -> Toast.makeText(activity, "Invalid label type.", Toast.LENGTH_SHORT).show()
            else -> {
                val printerTask = TemplatePrintTask(this, ip, mac, labelNameIndex, name, group, room, dateRange, recordNo)
                printerTask.execute()
            }
        }
    }

    override fun onProcessCompleted(message: String) {
        Toast.makeText(activityReference.get(), message, Toast.LENGTH_LONG).show()
    }
}