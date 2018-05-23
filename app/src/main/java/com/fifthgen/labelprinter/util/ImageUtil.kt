package com.fifthgen.labelprinter.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object ImageUtil {

    private const val TEXT_SIZE = 36.0f
    private const val TEXT_COLOR = Color.BLACK

    fun textAsBitMap(text: Array<String>): Bitmap? {

        if (text.isNotEmpty()) {
            val paint = Paint()
            paint.textSize = TEXT_SIZE
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.LEFT
            val baseline = -paint.ascent()
            val width = (paint.measureText(text[0]) + 0.5f).toInt()
            val height = (baseline + paint.descent() + 0.5f).toInt()
            val image = Bitmap.createBitmap(width + 500, height + 350, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawRect(0f, 0f, (width + 500).toFloat(), (height + 350).toFloat(), paint)
            paint.color = TEXT_COLOR

            for (i in text.indices) {
                canvas.drawText(text[i], 0f, baseline + i * 100, paint)
            }

            return image
        }

        return null
    }
}