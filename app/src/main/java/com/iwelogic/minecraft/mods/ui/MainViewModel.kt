package com.iwelogic.minecraft.mods.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.MobileAds
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.readString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : ViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)
    var openMain: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var openOnboarding: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun checkAge() {
        context.get()?.readString(Const.CONTENT_RATING)?.let {
            val requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setMaxAdContentRating(it)
                .setTestDeviceIds(listOf("5571260002C1C3A1FD32D49B3E5332C1", "81AC2F5CC6A169492DFD647D9F39B4AA"))
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            openMain.invoke(true)
        } ?: run {
            openOnboarding.invoke(true)
        }
    }
}