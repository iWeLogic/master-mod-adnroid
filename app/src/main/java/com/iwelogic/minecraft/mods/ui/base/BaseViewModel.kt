package com.iwelogic.minecraft.mods.ui.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwelogic.minecraft.mods.models.DialogData
import java.lang.ref.WeakReference

open class BaseViewModel(applicationContext: Context) : ViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)
    var progress: MutableLiveData<Boolean> = MutableLiveData(false)
    var error: MutableLiveData<String> = MutableLiveData("")
    val close: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showInterstitial: SingleLiveEvent<(() -> Unit)?> = SingleLiveEvent()
    val showDialog: SingleLiveEvent<DialogData> = SingleLiveEvent()

    fun onClickClose() {
        close.invoke(true)
    }

    fun onClickRetry() {
        onReload()
    }

    open fun onReload() {

    }

    fun getBase(): BaseViewModel = this
}