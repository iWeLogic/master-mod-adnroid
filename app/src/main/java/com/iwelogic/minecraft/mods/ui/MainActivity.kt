package com.iwelogic.minecraft.mods.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    var mInterstitialAd: InterstitialAd? = null
    private var mAdIsLoading: Boolean = false
    private var count = 0
    private var backStackSize: Int = 0

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.checkAge()
        binding.viewModel = viewModel
    }

    /*fun openHost() {
        val host = NavHostFragment.create(R.navigation.host)
        supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host)
            .setPrimaryNavigationFragment(host).commitAllowingStateLoss()
    }

    override fun openLoading() {
        loadInterstitialAd()
        val host = NavHostFragment.create(R.navigation.loading)
        supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host)
            .setPrimaryNavigationFragment(host).commitAllowingStateLoss()
    }

    override fun openSelectAge() {
        val host = NavHostFragment.create(R.navigation.select_age)
        supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host)
            .setPrimaryNavigationFragment(host).commitAllowingStateLoss()
    }
*/
    private fun loadInterstitialAd() {
        mAdIsLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.ad_interstitial),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    count++
                    if (count < 3) loadInterstitialAd()
                    else {
                        count = 0
                        mAdIsLoading = false
                    }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mAdIsLoading = false
                }
            })
    }

    fun showInterstitialAd(callback: (() -> Unit)? = null) {
        callback?.invoke()
        /* if (mInterstitialAd != null) {
             mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                 override fun onAdDismissedFullScreenContent() {

                 }

                 override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                     count = 0
                     callback?.invoke()
                     loadInterstitialAd()
                 }

                 override fun onAdShowedFullScreenContent() {
                     callback?.invoke()
                     mInterstitialAd = null
                     YandexMetrica.reportEvent("InterAdsShow")
                     count = 0
                     loadInterstitialAd()
                 }
             }
             mInterstitialAd?.show(this)
         } else {
             callback?.invoke()
             if (!mAdIsLoading) {
                 count=0
                 loadInterstitialAd()
             }
         }*/
    }
}