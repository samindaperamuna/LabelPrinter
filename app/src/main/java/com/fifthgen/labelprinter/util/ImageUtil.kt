package com.fifthgen.labelprinter.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.TypedValue


object ImageUtil {

    private const val TEXT_SIZE = 48.0f
    private const val TEXT_COLOR = Color.BLACK
    private const val LINE_HEIGHT = 1.5f

    fun textAsBitMap(text: Array<String>): Bitmap? {

        if (text.isNotEmpty()) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE, Resources.getSystem().displayMetrics)

            paint.textSize = pixel
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.LEFT

            val baseline = -paint.ascent() * LINE_HEIGHT

            val width = paint.measureText(getTextInOneLine(text) + 0.5f).toInt()
            val height = (baseline + paint.descent() + 0.5f).toInt()

            val image = Bitmap.createBitmap(width / text.size + 500, height * text.size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawRect(0f, 0f, (width / text.size + 500).toFloat(), (height * text.size).toFloat(), paint)
            paint.color = TEXT_COLOR

            for (i in text.indices) {
                val textOffset = (canvas.width - paint.measureText(text[i])) / 2
                canvas.drawText(text[i], textOffset, (i + 1) * baseline, paint)
            }

            return image
        }

        return null
    }

    private fun getTextInOneLine(text: Array<String>): String {
        var line = ""
        text.forEach { line += it }
        return line
    }

    private fun maxTextWidth(paint: Paint, text: Array<String>): Int {
        var maxWidth = 0

        for (string in text) {
            val width = (paint.measureText(string) + 0.0f).toInt()
            if (width > maxWidth) maxWidth = width
        }

        return maxWidth
    }
}