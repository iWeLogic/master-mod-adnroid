package com.iwelogic.minecraft.mods.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.io.InputStream

fun Uri.getBitmap(context: Context): Bitmap? {
    var inputStreamExif: InputStream? = null
    var inputStreamBitmap: InputStream? = null
    return try {
        inputStreamExif = context.contentResolver.openInputStream(this)
        inputStreamBitmap = context.contentResolver.openInputStream(this)
        val exifInterface = ExifInterface(inputStreamExif!!)
        var rotation = 0
        val orientation: Int = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
        }
        val matrix = Matrix()
        matrix.postRotate(rotation.toFloat())
        val bitmapOrg = BitmapFactory.decodeStream(inputStreamBitmap)
        Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.width, bitmapOrg.height, matrix, true)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    } finally {
        if (inputStreamExif != null) {
            try {
                inputStreamExif.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (inputStreamBitmap != null) {
            try {
                inputStreamBitmap.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}