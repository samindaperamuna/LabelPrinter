package com.fifthgen.labelprinter.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.brother.ptouch.sdk.LabelInfo
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.Session
import com.fifthgen.labelprinter.printer.PrinterHelper
import com.fifthgen.labelprinter.ui.custom.IPAddressTextWatcher
import com.fifthgen.labelprinter.util.Constants
import com.fifthgen.labelprinter.util.Constants.Companion.IP
import com.fifthgen.labelprinter.util.Constants.Companion.LABEL_MAKE
import com.fifthgen.labelprinter.util.Constants.Companion.LABEL_NAME_INDEX
import com.fifthgen.labelprinter.util.Constants.Companion.LABEL_TYPE
import com.fifthgen.labelprinter.util.Constants.Companion.MAC
import com.fifthgen.labelprinter.util.Constants.Companion.PARAM_OFFLINE
import com.fifthgen.labelprinter.util.Constants.Companion.URL
import com.phearme.macaddressedittext.MacAddressEditText
import java.util.*

class PrinterSettingsFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private lateinit var offlineSwitch: Switch
    private var labelMakeListSpinner: Spinner? = null
    private var labelTypeListSpinner: Spinner? = null
    private var ipTextInputLayout: TextInputLayout? = null
    private var ipTextInputEdit: TextInputEditText? = null
    private var macTextInputLayout: TextInputLayout? = null
    private var macTextInputEdit: MacAddressEditText? = null
    private lateinit var urlTextInputLayout: TextInputLayout
    private lateinit var urlTextInputEdit: TextInputEditText

    private lateinit var sharedPref: SharedPreferences
    private var labelTypes: MutableMap<Int, String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_printer_settings, container, false)

        offlineSwitch = view.findViewById(R.id.offlineSwitch)

        labelMakeListSpinner = view.findViewById(R.id.labelMakeListSpinner)
        labelMakeListSpinner!!.onItemSelectedListener = this

        labelTypeListSpinner = view.findViewById(R.id.labelTypeListSpinner)

        ipTextInputLayout = view.findViewById(R.id.ipTextInputLayout)

        ipTextInputEdit = view.findViewById(R.id.ipTextInputEdit)
        ipTextInputEdit!!.addTextChangedListener(IPAddressTextWatcher())

        macTextInputLayout = view.findViewById(R.id.macTextInputLayout)
        macTextInputEdit = view.findViewById(R.id.macTextInputEdit)

        urlTextInputLayout = view.findViewById(R.id.urlTextInputLayout)
        urlTextInputEdit = view.findViewById(R.id.urlTextInputEdit)

        val saveButton = view.findViewById<ImageButton>(R.id.saveButton)
        saveButton.setOnClickListener(this)

        val testButton = view.findViewById<ImageButton>(R.id.testButton)
        testButton.setOnClickListener(this)

        sharedPref = context!!.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        loadPreferences()

        return view
    }

    private fun loadPreferences() {
        val activity = activity

        if (activity != null) {
            val labelMake = sharedPref.getString(LABEL_MAKE, "")

            offlineSwitch.isChecked = sharedPref.getBoolean(PARAM_OFFLINE, false)
            val ip = sharedPref.getString(IP, "")
            val mac = sharedPref.getString(MAC, "")
            val url = sharedPref.getString(URL, "")

            if (labelMake!!.isNotEmpty()) {
                val adapter = labelMakeListSpinner!!.adapter
                if (adapter != null) {
                    for (i in 0 until adapter.count) {
                        if (labelMake == adapter.getItem(i)) {
                            labelMakeListSpinner!!.setSelection(i)
                            break
                        }
                    }
                }
            }

            if (!ip!!.isEmpty()) {
                ipTextInputEdit!!.setText(ip)
            }

            if (!mac!!.isEmpty()) {
                macTextInputEdit!!.setText(mac)
            }

            if (url.isNotEmpty()) {
                urlTextInputEdit.setText(url)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        if (parent.id == R.id.labelMakeListSpinner) {
            val selection = parent.getItemAtPosition(pos).toString()
            labelTypes = TreeMap()

            when (selection) {
                "PT" -> for (type in LabelInfo.PT.values()) {
                    labelTypes!![type.ordinal] = type.name
                }
                "QL700" -> for (type in LabelInfo.QL700.values()) {
                    labelTypes!![type.ordinal] = type.name
                }
                "QL1100" -> for (type in LabelInfo.QL1100.values()) {
                    labelTypes!![type.ordinal] = type.name
                }
                "PT3" -> for (type in LabelInfo.PT3.values()) {
                    labelTypes!![type.ordinal] = type.name
                }
                "QL1115" -> for (type in LabelInfo.QL1115.values()) {
                    labelTypes!![type.ordinal] = type.name
                }
            }

            val adapter = ArrayAdapter(parent.context.applicationContext,
                    android.R.layout.simple_spinner_item, labelTypes!!.values.toTypedArray())
            labelTypeListSpinner!!.adapter = adapter

            val activity = activity

            if (activity != null) {
                val labelType = sharedPref.getString(LABEL_TYPE, "")

                if (labelType!!.isNotEmpty()) {
                    for (i in 0 until labelTypeListSpinner!!.count) {
                        if (labelType == labelTypeListSpinner!!.getItemAtPosition(i)) {
                            labelTypeListSpinner!!.setSelection(i)
                            break
                        }
                    }
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        if (parent.id == R.id.labelMakeListSpinner) {
            if (!labelTypeListSpinner!!.adapter.isEmpty)
                labelTypeListSpinner!!.setSelection(0)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.saveButton -> if (validateForm()) {
                val activity = activity

                if (activity != null) {
                    val sharedPref = context!!.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()

                    val session = context!!.applicationContext as Session
                    session.offline = offlineSwitch.isChecked

                    editor.putBoolean(PARAM_OFFLINE, session.offline)

                    editor.putString(LABEL_MAKE, labelMakeListSpinner!!.selectedItem.toString())
                    editor.putString(LABEL_TYPE, labelTypeListSpinner!!.selectedItem.toString())

                    val item = labelTypeListSpinner!!.selectedItem.toString()

                    for (key in labelTypes!!.keys) {
                        if (labelTypes!![key] == item) {
                            editor.putInt(LABEL_NAME_INDEX, key)
                            break
                        }
                    }

                    editor.putString(IP, ipTextInputEdit!!.text.toString())
                    editor.putString(MAC, macTextInputEdit!!.text.toString())
                    editor.putString(URL, urlTextInputEdit.text.toString())

                    editor.apply()
                    Toast.makeText(activity, "Settings saved successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.testButton -> {
                val activity = activity

                if (activity != null) {
                    val text = arrayOf("Test User", "Test Company", "Test Position")
                    val printerHelper = PrinterHelper(activity)
                    printerHelper.print(text)
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        if (ipTextInputEdit!!.text.toString() == "") {
            ipTextInputLayout!!.error = getString(R.string.empty_ip)
            return false
        } else if (!ipTextInputEdit!!.text.toString().matches(Patterns.IP_ADDRESS.toString().toRegex())) {
            ipTextInputLayout!!.error = getString(R.string.invalid_ip)
            return false
        } else if (macTextInputEdit!!.text.toString() == "") {
            macTextInputLayout!!.error = getString(R.string.empty_mac)
            return false
        } else if (urlTextInputEdit.text.toString() == "") {
            urlTextInputLayout.error = getString(R.string.empty_url)
            return false
        } else if (!urlTextInputEdit.text.toString().matches(Constants.URL_PATTERN.toRegex())) {
            urlTextInputLayout.error = getString(R.string.invalid_url)
            return false
        }

        return true
    }
}