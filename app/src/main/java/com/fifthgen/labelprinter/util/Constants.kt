package com.fifthgen.labelprinter.util

class Constants {

    companion object {
        const val POUND_SYMBOL = '#'
        const val NET_SYMBOL = '\u21C5'
        val ALPHABET = ('A'..'Z').mapIndexed { index, c -> index to c }.toMap()

        // Main Activity constants.
        const val ROWS = 7
        const val COLS = 4
    }
}