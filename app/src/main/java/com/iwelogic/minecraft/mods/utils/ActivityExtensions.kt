package com.iwelogic.minecraft.mods.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowMetrics
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard(clearFocus: Boolean) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    window.currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        if (clearFocus) {
            it.clearFocus()
        }
    }
}

fun Activity.showKeyboard(view: View?) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.requestFocus()
    view?.postDelayed({
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }, 100)
}

fun Activity?.openUrl(url: String) {
    catchAll {
        this?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

fun Activity?.getScreenWidth() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    this?.let {
        val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } ?: run {
        0
    }
} else {
    this?.let {
        val displayMetrics = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    } ?: run {
        0
    }
}

fun Activity?.getScreenHeight() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    this?.let {
        val windowMetrics: WindowMetrics = this.windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.top - insets.bottom
    } ?: run {
        0
    }
} else {
    this?.let {
        val displayMetrics = DisplayMetrics()
        this.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    } ?: run {
        0
    }
}

