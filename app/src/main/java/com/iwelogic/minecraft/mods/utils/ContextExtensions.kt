package com.iwelogic.minecraft.mods.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

fun Int.dp(context: Context?): Int {
    return (toFloat() * (context?.resources?.displayMetrics?.density ?: 1f)).toInt()
}

fun Context.getUserCountry(): String {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = tm.simCountryIso
        if (simCountry != null && simCountry.length == 2) { // SIM country code is available
            return simCountry.lowercase(Locale.US)
        } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
            val networkCountry = tm.networkCountryIso
            if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                return networkCountry.lowercase(Locale.US)
            }
        }
    } catch (e: Exception) {
    }
    return Locale.getDefault().country
}

@SuppressLint("PackageManagerGetSignatures")
fun Context.getSHA1FingerPrint() {
    val info: PackageInfo
    try {
        info = packageManager.getPackageInfo(applicationContext.packageName, PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val hash = String(Base64.encode(md.digest(), 0))
            Log.e("hash", hash)
        }
    } catch (e1: PackageManager.NameNotFoundException) {
        Log.e("name not found", e1.toString())
    } catch (e: NoSuchAlgorithmException) {
        Log.e("no such an algorithm", e.toString())
    } catch (e: java.lang.Exception) {
        Log.e("exception", e.toString())
    }
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}