package com.fifthgen.labelprinter.printer

import android.os.AsyncTask
import android.util.Log
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo

class TemplatePrintTask(private val asyncResponse: AsyncResponse,
                        private val ip: String,
                        private val mac: String,
                        private val labelNameIndex: Int,
                        private val name: String,
                        private val group: String,
                        private val room: String,
                        private val dateRange: String,
                        private val recordNo: Int) : AsyncTask<Unit, Unit, String>() {

    private lateinit var printer: Printer
    private lateinit var message: String

    override fun doInBackground(params: Array<Unit>): String {
        if (setupPrinter(ip, mac, labelNameIndex)) {
            print()
        }

        return message
    }

    override fun onPostExecute(message: String) {
        asyncResponse.onProcessCompleted(message)
    }

    private fun setupPrinter(ip: String, mac: String, labelNameIndex: Int): Boolean {

        try {
            printer = Printer()
            printer.startPTTPrint(1, null)
            printer.replaceTextName(room, "objName")
            printer.replaceTextName(name, "objCompany")
            printer.replaceTextName(group, "objDossier")
            printer.replaceTextName(dateRange, "objDate")
            printer.replaceTextName(recordNo.toString(), "BarCode7")

            val printerInfo = printer.printerInfo

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

            printer.printerInfo = printerInfo

            return true
        } catch (e: Exception) {
            Log.e(javaClass.name, "Couldn't initialize the printer.")
        }

        return false
    }

    private fun print() {
        printer.startCommunication()
        val printerStatus = printer.flushPTTPrint()
        printer.endCommunication()

        if (printerStatus.errorCode !== PrinterInfo.ErrorCode.ERROR_NONE) {
            message = "Couldn't print label. Error code : " + printerStatus.errorCode
            Log.e(javaClass.name, message)
        } else {
            message = "All documents successfully sent to printer."
        }
    }

}