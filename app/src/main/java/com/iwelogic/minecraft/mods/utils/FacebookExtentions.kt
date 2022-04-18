package com.iwelogic.minecraft.mods.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest

fun Context.generateKeyHashes(): String {
    return try {
        @SuppressLint("PackageManagerGetSignatures") val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        val md = MessageDigest.getInstance("SHA")
        md.update(info.signatures[(info.signatures.indices).random()].toByteArray())
        Base64.encodeToString(md.digest(), Base64.DEFAULT)
    } catch (ignored: Exception) {
        ""
    }
}