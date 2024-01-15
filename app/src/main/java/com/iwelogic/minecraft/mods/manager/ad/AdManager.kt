package com.iwelogic.minecraft.mods.manager.ad

import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.manager.FirebaseConfigManager
import com.iwelogic.minecraft.mods.ui.MainActivity
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.utils.readString
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.*

@Singleton
class AdManager @Inject constructor(
    @ApplicationContext val applicationContext: Context,
    private val firebaseConfigManager: FirebaseConfigManager
) {

    private val mAdIsLoading = MutableStateFlow(false)
    var googleInterstitial: InterstitialAd? = null
    var callback: (() -> Unit)? = null
    lateinit var activity: MainActivity
    var canRequestAd = false

    fun enableAdd() {
        canRequestAd = true
        applicationContext.readString(Const.CONTENT_RATING)?.let { contentRating ->
            setContentRating(contentRating)
        }
    }

    fun setContentRating(contentRating: String) {
        if (!canRequestAd) return
        val requestConfiguration = MobileAds.getRequestConfiguration()
            .toBuilder()
            .setMaxAdContentRating(contentRating)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)
        MobileAds.initialize(applicationContext)
        CoroutineScope(Dispatchers.Main).launch {
            loadAdd()
        }
    }

    private fun loadAdd() {
        if (!canRequestAd) return
        mAdIsLoading.value = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            applicationContext,
            applicationContext.getString(R.string.ad_interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    mAdIsLoading.value = false
                    googleInterstitial = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    googleInterstitial = interstitialAd
                    mAdIsLoading.value = false
                }
            })
    }

    fun showInterstitialAd(adUnit: AdUnit, callback: (() -> Unit)? = null) {
        if(!firebaseConfigManager.getAdUnitStatus(adUnit)){
            callback?.invoke()
            return
        }
        if (googleInterstitial != null) {
            googleInterstitial?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {

                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    callback?.invoke()
                    loadAdd()
                }

                override fun onAdShowedFullScreenContent() {
                    callback?.invoke()
                    googleInterstitial = null
                    loadAdd()
                }
            }
            googleInterstitial?.show(activity)
        } else {
            callback?.invoke()
            if (!mAdIsLoading.value) {
                loadAdd()
            }
        }
    }
}