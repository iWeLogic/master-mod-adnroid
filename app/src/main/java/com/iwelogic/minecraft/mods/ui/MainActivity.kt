package com.iwelogic.minecraft.mods.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    var mInterstitialAd: InterstitialAd? = null
    private var mAdIsLoading: Boolean = false
    private var count = 0


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        var keep = true
        splashScreen.setKeepOnScreenCondition { keep }
        lifecycleScope.launch(Dispatchers.Default) {
            delay(1000)
            keep = false
        }
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.checkAge()
        binding.viewModel = viewModel

        viewModel.openMain.observe(this) {
            openMain()
        }

        viewModel.openOnboarding.observe(this) {
            val host = NavHostFragment.create(R.navigation.onboarding)
            supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host)
                .setPrimaryNavigationFragment(host).commitAllowingStateLoss()
        }
    }

    fun openMain() {
        loadInterstitialAd()
        val host = NavHostFragment.create(R.navigation.main)
        supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host)
            .setPrimaryNavigationFragment(host).commitAllowingStateLoss()
    }

    private fun loadInterstitialAd() {
        mAdIsLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.ad_interstitial), adRequest, object : InterstitialAdLoadCallback() {
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
        if (mInterstitialAd != null) {
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
                    count = 0
                    loadInterstitialAd()
                }
            }
            mInterstitialAd?.show(this)
        } else {
            callback?.invoke()
            if (!mAdIsLoading) {
                count = 0
                loadInterstitialAd()
            }
        }
    }
}