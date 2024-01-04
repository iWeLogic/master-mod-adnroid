package com.iwelogic.minecraft.mods.ui.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.ump.UserMessagingPlatform
import com.iwelogic.minecraft.mods.ui.base.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val isPrivacyPolicyVisible: MutableLiveData<Boolean> = MutableLiveData(UserMessagingPlatform.getConsentInformation(applicationContext).isConsentFormAvailable)
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
