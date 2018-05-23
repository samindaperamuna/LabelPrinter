package com.fifthgen.labelprinter.ui.custom

import android.text.Editable
import android.text.TextWatcher
import java.util.regex.Pattern

class IPAddressTextWatcher : TextWatcher {

    private var mPreviousText: String? = null

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        if (PARTIAL_IP_ADDRESS.matcher(editable).matches()) {
            mPreviousText = editable.toString()
        } else {
            editable.replace(0, editable.length, mPreviousText)
        }
    }

    companion object {
        private val PARTIAL_IP_ADDRESS =
                Pattern.compile("^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}"
                        + "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))?$")
    }
}