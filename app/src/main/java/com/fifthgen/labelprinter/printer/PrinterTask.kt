package com.fifthgen.labelprinter.printer

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.fifthgen.labelprinter.util.ImageUtil

class PrinterTask(private val asyncResponse: AsyncResponse,
                  private val ip: String,
                  private val mac: String,
                  private val labelNameIndex: Int) : AsyncTask<String, Void, String>() {

    private var printer: Printer? = null
    private var message: String? = null

    override fun doInBackground(params: Array<String>): String? {

        if (params.isNotEmpty()) {
            if (setupPrinter(ip, mac, labelNameIndex)) {
                val imageToPrint = ImageUtil.textAsBitMap(params)

                print(imageToPrint)
            }
        }

        return message
    }

    override fun onPostExecute(message: String) {
        asyncResponse.onProcessCompleted(message)
    }

    private fun setupPrinter(ip: String, mac: String, labelNameIndex: Int): Boolean {

        try {
            printer = Printer()

            val printerInfo = printer!!.printerInfo

            printerInfo.printerModel = PrinterInfo.Model.QL_820NWB
            printerInfo.port = PrinterInfo.Port.NET
            printerInfo.paperSize = PrinterInfo.PaperSize.CUSTOM
            printerInfo.orientation = PrinterInfo.Orientation.LANDSCAPE
            printerInfo.valign = PrinterInfo.VAlign.MIDDLE
            printerInfo.align = PrinterInfo.Align.CENTER
            printerInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAPER
            printerInfo.numberOfCopies = 1
            printerInfo.macAddress = mac
            printerInfo.ipAddress = ip

            printerInfo.labelNameIndex = labelNameIndex

            printerInfo.isAutoCut = true
            printerInfo.isCutAtEnd = false
            printerInfo.isHalfCut = false
            printerInfo.isSpecialTape = false

            printer!!.printerInfo = printerInfo

            return true
        } catch (e: Exception) {
            Log.e(javaClass.name, "Couldn't initialize the printer.")
        }

        return false
    }

    private fun print(image: Bitmap?) {
        printer!!.startCommunication()
        val printerStatus = printer!!.printImage(image)
        printer!!.endCommunication()

        if (printerStatus.errorCode !== PrinterInfo.ErrorCode.ERROR_NONE) {
            message = "Couldn't print label. Error code : " + printerStatus.errorCode
            Log.e(javaClass.name, message)
        } else {
            message = "All documents successfully sent to printer."
        }
    }
}
