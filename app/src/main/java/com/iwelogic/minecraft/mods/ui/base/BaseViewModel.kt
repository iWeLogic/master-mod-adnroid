package com.iwelogic.minecraft.mods.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    var progress: MutableLiveData<Boolean> = MutableLiveData(false)
    var error: MutableLiveData<Boolean> = MutableLiveData(false)
    val close: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun onClickClose() {
        close.invoke(true)
    }

    fun onClickRetry() {
        reload()
    }

    open fun reload() {

    }

    fun getBase(): BaseViewModel = this
}