package com.iwelogic.minecraft.mods.utils

import android.app.Activity
import android.content.*
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson

fun Int.dp(context: Context?): Int {
    return (toFloat() * (context?.resources?.displayMetrics?.density ?: 1f)).toInt()
}

fun Int.fromPxToDp(context: Context?): Int {
    return (toFloat() / (context?.resources?.displayMetrics?.density ?: 1f)).toInt()
}

fun <T> LiveData<T>.ignoreFirst(): MutableLiveData<T> {
    val result = MediatorLiveData<T>()
    var isFirst = true
    result.addSource(this) {
        if (isFirst) isFirst = false
        else result.value = it
    }
    return result
}

inline fun <reified T> List<T>?.deepCopy(): List<T>? {
    val temp: MutableList<T> = ArrayList()
    val gson = Gson()
    this?.forEach {
        temp.add(gson.fromJson(gson.toJson(it), T::class.java))
    }
    return if (this == null) null else temp.toList()
}

fun Boolean?.isTrue(action: () -> Unit) {
    if (this == true) {
        action.invoke()
    }
}

fun Boolean?.isTrue(): Boolean {
    return this == true
}

fun Activity.hideKeyboard(clearFocus: Boolean) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    window.currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        if (clearFocus) {
            it.clearFocus()
        }
    }
}

fun Context?.logEvent(event: String, bundle: Bundle = Bundle()) {
    this?.let { FirebaseAnalytics.getInstance(it).logEvent(event, bundle) }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelableArray(key: String): List<T>? = when {
    SDK_INT >= 33 -> getParcelableArrayExtra(key, T::class.java)?.toList()
    else -> @Suppress("DEPRECATION") (getParcelableArrayExtra(key))?.map { it as T }
}

inline fun <reified T : Parcelable> Bundle.parcelableArray(key: String): List<T>? = when {
    SDK_INT >= 33 -> getParcelableArray(key, T::class.java)?.toList()
    else -> @Suppress("DEPRECATION") (getParcelableArray(key))?.map { it as T }
}

