package com.iwelogic.minecraft.mods.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

open class BaseViewModel(application: Context) : ViewModel() {

    var progress: MutableLiveData<Boolean> = MutableLiveData(false)
    var error: MutableLiveData<Boolean> = MutableLiveData(false)
    var context: WeakReference<Context> = WeakReference(application)

    fun onClickClose() {
      //  navigator?.close()
    }

    fun onClickRetry() {
        reload()
    }

    open fun reload() {

    }

    fun getBase(): BaseViewModel = this
}