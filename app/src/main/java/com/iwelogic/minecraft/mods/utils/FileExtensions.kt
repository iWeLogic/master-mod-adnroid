package com.iwelogic.minecraft.mods.utils

import android.content.Context
import android.text.TextUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun File.checkMD5(md5: String?, updateFile: File?): Boolean {
    if (TextUtils.isEmpty(md5) || updateFile == null) {
        return false
    }
    val calculatedDigest = calculateMD5(updateFile) ?: return false
    return calculatedDigest.equals(md5, true)
}

private fun calculateMD5(updateFile: File?): String? {

    val digest: MessageDigest = try {
        MessageDigest.getInstance("MD5")
    } catch (e: NoSuchAlgorithmException) {
        return null
    }
    val inputStream = updateFile?.let {
        if (!it.exists()) return null
        FileInputStream(it)
    } ?: run {
        return null
    }

    val buffer = ByteArray(8192)
    var read: Int
    return try {
        while (inputStream.read(buffer).also { read = it } > 0) {
            digest.update(buffer, 0, read)
        }
        val md5sum = digest.digest()
        val bigInt = BigInteger(1, md5sum)
        var output: String = bigInt.toString(16)
        output = String.format("%32s", output).replace(' ', '0')
        output
    } catch (e: Exception) {
        throw RuntimeException("Unable to process file for MD5", e)
    } finally {
        try {
            inputStream.close()
        } catch (e: Exception) {
        }
    }
}

fun Context.readFileFromRawFolder(id: Int): String {
    val inputStream = InputStreamReader(resources.openRawResource(id))
    val br = BufferedReader(inputStream)
    var receiveString: String
    val stringBuilder = StringBuilder()
    while (br.readLine().also { receiveString = it } != null) {
        stringBuilder.append(receiveString)
    }
    inputStream.close()
    return stringBuilder.toString()
}