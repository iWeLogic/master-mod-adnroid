package com.iwelogic.minecraft.mods.ui.settings

import android.content.Context
import com.iwelogic.minecraft.mods.ui.base.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val share: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val rate: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val openGDPR: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun onClickPrivacySettings() {
        openGDPR.invoke(true)
    }

    fun onClickShare() {
        share.invoke(true)
    }

    fun onClickRate() {
        rate.invoke(true)
    }
}
