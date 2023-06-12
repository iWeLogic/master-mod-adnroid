package com.iwelogic.minecraft.mods.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.MobileAds
import com.iwelogic.minecraft.mods.models.Advertisement
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.readString
import com.iwelogic.minecraft.mods.utils.writeBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext applicationContext: Context
) : ViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)
    var openMain: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var openOnboarding: SingleLiveEvent<Boolean> = SingleLiveEvent()

    init {
        loadAdd()
    }

    fun checkAge() {
        context.get()?.readString(Const.CONTENT_RATING)?.let {
            val requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setMaxAdContentRating(it)
                .setTestDeviceIds(listOf("5571260002C1C3A1FD32D49B3E5332C1"))
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            openMain.invoke(true)
        } ?: run {
            openOnboarding.invoke(true)
        }
    }

    private fun loadAdd() {
        context.get()?.writeBoolean(Advertisement.NATIVE_ON_DETAILS.id, true)
        context.get()?.writeBoolean(Advertisement.INTERSTITIAL_OPEN_DETAILS.id, true)
        context.get()?.writeBoolean(Advertisement.INTERSTITIAL_OPEN_SEARCH.id, true)
        context.get()?.writeBoolean(Advertisement.INTERSTITIAL_OPEN_FAVORITE.id, true)
        context.get()?.writeBoolean(Advertisement.BANNER_IN_LIST.id, false)
    }
}