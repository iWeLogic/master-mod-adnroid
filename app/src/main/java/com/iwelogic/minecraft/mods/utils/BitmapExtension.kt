package com.iwelogic.minecraft.mods.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.math.roundToInt

fun Bitmap?.writeToFile(context: Context): File? {
    val directory = File(context.filesDir.toString() + "/images")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val compressFile = File(directory.path + "/" + System.currentTimeMillis() + ".jpg")
    try {
        val out = FileOutputStream(compressFile.absolutePath)
        this!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
    } catch (e: FileNotFoundException) {
        return null
    }
    return compressFile
}


fun Bitmap?.compress(max: Int): Bitmap? {
    if (this == null) return null
    return if (this.width > this.height) {
        val ratio: Float = this.width.toFloat() / this.height.toFloat()
        val height: Int = (max / ratio).roundToInt()
        Bitmap.createScaledBitmap(this, max, height, false)
    } else {
        val ratio: Float = this.height.toFloat() / this.width.toFloat()
        val width: Int = (max / ratio).roundToInt()
        Bitmap.createScaledBitmap(this, width, max, false)
    }
}

fun Bitmap?.decodeToBase64(): String? {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
}