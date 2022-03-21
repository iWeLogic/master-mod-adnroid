package com.iwelogic.minecraft.mods.utils

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat

/*fun EditText.showError(text: String?) {
    text?.let {
        val icon = ContextCompat.getDrawable(context, R.drawable.error)
        icon?.let {
            it.bounds = Rect(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            requestFocus()
            setError(text, icon)
        }
    } ?: run {
        setError(null, null)
    }
}*/

fun String.splitWordsIntoStringsThatFit(width: Float, textSize: Float, font: Int, context: Context): List<String> {
    val paint = Paint()
    paint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, context.resources.displayMetrics)
    paint.typeface = ResourcesCompat.getFont(context, font)

    val result: ArrayList<String> = ArrayList()
    val currentLine: ArrayList<String?> = ArrayList()
    val sources = split(" ").toTypedArray()
    for (chunk in sources) {
        if (paint.measureText(chunk) < width) {
            processFitChunk(width, paint, result, currentLine, chunk)
        } else {
            val splitChunk = splitIntoStringsThatFit(chunk, width, paint)
            for (chunkChunk in splitChunk) {
                processFitChunk(width, paint, result, currentLine, chunkChunk)
            }
        }
    }
    if (!currentLine.isEmpty()) {
        result.add(TextUtils.join(" ", currentLine))
    }
    return result
}

private fun splitIntoStringsThatFit(source: String, maxWidthPx: Float, paint: Paint): List<String> {
    if (TextUtils.isEmpty(source) || paint.measureText(source) <= maxWidthPx) {
        return listOf(source)
    }
    val result: ArrayList<String> = ArrayList()
    var start = 0
    for (i in 1..source.length) {
        val subStr = source.substring(start, i)
        if (paint.measureText(subStr) >= maxWidthPx) {
            val fits = source.substring(start, i - 1)
            result.add(fits)
            start = i - 1
        }
        if (i == source.length) {
            val fits = source.substring(start, i)
            result.add(fits)
        }
    }
    return result
}

private fun processFitChunk(maxWidth: Float, paint: Paint, result: ArrayList<String>, currentLine: ArrayList<String?>, chunk: String) {
    currentLine.add(chunk)
    val currentLineStr = TextUtils.join(" ", currentLine)
    if (paint.measureText(currentLineStr) >= maxWidth) {
        currentLine.removeAt(currentLine.size - 1)
        result.add(TextUtils.join(" ", currentLine))
        currentLine.clear()
        currentLine.add(chunk)
    }
}