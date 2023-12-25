package com.iwelogic.minecraft.mods.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.*
import com.google.android.ump.*
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.databinding.ActivityMainBinding
import com.iwelogic.minecraft.mods.manager.AdManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var consentInformation: ConsentInformation

    @Inject
    lateinit var adManager: AdManager

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
        adManager.activity = this
        viewModel.openMain.observe(this) {
            openMain()
        }

        viewModel.openOnboarding.observe(this) {
            val host = NavHostFragment.create(R.navigation.onboarding)
            supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host).setPrimaryNavigationFragment(host).commitAllowingStateLoss()
        }


//        val debugSettings = ConsentDebugSettings.Builder(this)
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .addTestDeviceHashedId("D24A44F786A3570897EF8B5ABCE9EBC2")
//            .build()

        val params = ConsentRequestParameters
            .Builder()
//            .setConsentDebugSettings(debugSettings)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(this, params, {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                this@MainActivity
            ) {
                if (consentInformation.canRequestAds()) {
                    adManager.enableAdd(this)
                }
            }
        }, {
        })
        if (consentInformation.canRequestAds()) {
            adManager.enableAdd(this)

        }
    }

    fun openMain() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostContainer) as NavHostFragment
        val currentFragment = navHostFragment.navController.currentDestination?.id
        if (currentFragment == null || currentFragment == R.id.onboardingFragment) {
            val host = NavHostFragment.create(R.navigation.main)
            supportFragmentManager.beginTransaction().replace(R.id.hostContainer, host).setPrimaryNavigationFragment(host).commitAllowingStateLoss()
        }
    }
}