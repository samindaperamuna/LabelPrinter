package com.fifthgen.labelprinter.ui.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.fifthgen.labelprinter.R
import com.fifthgen.labelprinter.printer.PrinterHelper
import com.fifthgen.labelprinter.util.ImageUtil

class LabelSettingsFragment : Fragment(), View.OnClickListener {

    private var previewImageView: ImageView? = null

    private var nameInputLayout: TextInputLayout? = null
    private var nameEditText: TextInputEditText? = null
    private var companyInputLayout: TextInputLayout? = null
    private var companyEditText: TextInputEditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_label_settings, container, false)

        previewImageView = view.findViewById(R.id.previewImageView)

        nameInputLayout = view.findViewById(R.id.nameInputLayout)
        nameEditText = view.findViewById(R.id.nameEditText)
        companyInputLayout = view.findViewById(R.id.companyInputLayout)
        companyEditText = view.findViewById(R.id.companyEditText)

        val previewButton = view.findViewById<ImageButton>(R.id.previewButton)
        previewButton.setOnClickListener(this)

        val printButton = view.findViewById<ImageButton>(R.id.printButton)
        printButton.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.previewButton -> if (validateForm()) {
                val text = arrayOf(nameEditText!!.text.toString(), companyEditText!!.text.toString())
                val bitmap = ImageUtil.textAsBitMap(text)
                previewImageView!!.setImageBitmap(bitmap)
                previewImageView!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            R.id.printButton -> if (validateForm()) {
                val text = arrayOf(nameEditText!!.text.toString(), companyEditText!!.text.toString())

                val activity = activity

                if (activity != null) {
                    val printerHelper = PrinterHelper(activity)
                    printerHelper.print(text)
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        return when {
            nameEditText!!.text.toString() == "" -> {
                nameInputLayout!!.error = getString(R.string.full_name_error)
                false
            }
            companyEditText!!.text.toString() == "" -> {
                companyInputLayout!!.error = getString(R.string.company_error)
                false
            }
            else -> true
        }
    }
}