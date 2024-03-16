package com.iwelogic.minecraft.mods.ui.onboarding

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.*
import com.google.android.gms.ads.RequestConfiguration.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.iwelogic.minecraft.mods.ui.base.*
import com.iwelogic.minecraft.mods.ui.base.Const.VALUE
import com.iwelogic.minecraft.mods.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(@ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    var age: MutableLiveData<Int> = MutableLiveData(45)
    var openMain: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var openUrl: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var selected: MutableLiveData<Boolean> = MutableLiveData(false)
    private val ageObserver: (Int) -> Unit = {
        selected.postValue(it > 0)
    }

    init {
        val params = Bundle()
        FirebaseAnalytics.getInstance(applicationContext).logEvent("open_onboarding", params)
    }

    fun subscribeOnAgeChanges() {
        age.ignoreFirst().observeForever(ageObserver)
    }

    fun onClickOk() {
        val contentRating = when (age.value) {
            in 0..6 -> MAX_AD_CONTENT_RATING_G
            in 7..11 -> MAX_AD_CONTENT_RATING_PG
            in 12..17 -> MAX_AD_CONTENT_RATING_T
            else -> MAX_AD_CONTENT_RATING_MA
        }
        val params = Bundle()
        params.putString(VALUE, contentRating)
        context.get()?.let { FirebaseAnalytics.getInstance(it).logEvent(Const.CONTENT_RATING, params) }
        context.get()?.writeString(Const.CONTENT_RATING, contentRating)
        context.get()?.writeString(Const.AGE, age.value.toString())
        val requestConfiguration = MobileAds
            .getRequestConfiguration()
            .toBuilder()
            .setMaxAdContentRating(contentRating)
            .setTestDeviceIds(listOf("5571260002C1C3A1FD32D49B3E5332C1", "81AC2F5CC6A169492DFD647D9F39B4AA"))
        if (contentRating == MAX_AD_CONTENT_RATING_G) {
            requestConfiguration.setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
        }
        MobileAds.setRequestConfiguration(requestConfiguration.build())
        openMain.invoke(true)
    }

    fun onClickPrivacyPolicy() {
        openUrl.invoke(true)
    }

    override fun onCleared() {
        super.onCleared()
        age.removeObserver(ageObserver)
    }
}
