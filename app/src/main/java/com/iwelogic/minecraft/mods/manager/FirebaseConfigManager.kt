package com.iwelogic.minecraft.mods.manager

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.*
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.iwelogic.minecraft.mods.App
import com.iwelogic.minecraft.mods.manager.ad.AdUnit
import kotlinx.coroutines.flow.*
import javax.inject.*

@Singleton
class FirebaseConfigManager @Inject constructor() {

    companion object {
        private const val KEY_BASE_URL = "base_url"
        private const val KEY_SHOW_CONTENT_RATING = "show_content_rating"
    }

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> get() = _isLoaded

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        Firebase.remoteConfig.apply {
            setDefaultsAsync(
                mapOf(
                    KEY_BASE_URL to "https://master-mod.s3.eu-central-1.amazonaws.com",
                    KEY_SHOW_CONTENT_RATING to true,
                    AdUnit.OPEN_SEARCH.code to false,
                    AdUnit.OPEN_FAVORITE.code to false,
                    AdUnit.OPEN_DETAILS.code to false,
                    AdUnit.NATIVE_ON_DETAILS.code to false,
                    AdUnit.BANNER_IN_LIST.code to false,
                )
            )
        }
    }

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(KEY_BASE_URL)) {
                    remoteConfig.activate().addOnCompleteListener {
                        App.baseUrl = remoteConfig.getString(KEY_BASE_URL)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {

            }
        })
        val cacheExpiration = 0L
        remoteConfig.fetch(cacheExpiration).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activate().addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoaded.value = true
                        App.baseUrl = remoteConfig.getString(KEY_BASE_URL)
                    } else {
                        _isLoaded.value = true
                        App.baseUrl = "https://master-mod.s3.eu-central-1.amazonaws.com"
                    }
                }
            } else {
                _isLoaded.value = true
                App.baseUrl = "https://master-mod.s3.eu-central-1.amazonaws.com"
            }
        }
    }

    fun getAdUnitStatus(adUnit: AdUnit) = remoteConfig.getBoolean(adUnit.code)

    fun isContentRatingShown() = remoteConfig.getBoolean(KEY_SHOW_CONTENT_RATING)
}