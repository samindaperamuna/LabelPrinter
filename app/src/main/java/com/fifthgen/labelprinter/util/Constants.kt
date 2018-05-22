package com.fifthgen.labelprinter.util

class Constants {

    companion object {
        const val POUND_SYMBOL = '#'
        const val NET_SYMBOL = '\u21C5'
        const val REMOTE_URL = "http://etiolles.club/deroule/rooming/get_all_roomilist_participant"
        const val APP_PREFERENCES = "labelPrinterPref"
        const val DATE_PATTERN = "dd/MM/yyyy"

        val ALPHABET = ('A'..'Z').mapIndexed { index, c -> index to c }.toMap()

        // Main Activity constants.
        const val ROWS = 7
        const val COLS = 4

        // Intent extras parameters
        const val PARAM_OFFLINE = "offline"
        const val PARAM_DATE = "date"
        const val PARAM_RECORDS = "records"
        const val PARAM_INDEX = "index"
        const val PARAM_ERROR = "error"
        const val PARAM_MESSAGE = "message"

        // Intent actions
        const val BROADCAST_SUCCESS_ACTION = "broadcastSuccessAction"
        const val BROADCAST_FAIL_ACTION = "broadcastFailAction"
    }
}