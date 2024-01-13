package com.iwelogic.minecraft.mods.ui

import android.content.Context
import androidx.lifecycle.*
import com.iwelogic.minecraft.mods.manager.*
import com.iwelogic.minecraft.mods.manager.ad.AdManager
import com.iwelogic.minecraft.mods.ui.base.*
import com.iwelogic.minecraft.mods.utils.readString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext applicationContext: Context,
    private val adManager: AdManager,
    private val firebaseConfigManager: FirebaseConfigManager
) : ViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)
    var openMain: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var openOnboarding: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var isAnimationEnabled = true

    fun checkAge() {
        viewModelScope.launch(Dispatchers.Default) {
            firebaseConfigManager.isLoaded.collect {
                if (it) {
                    context.get()?.readString(Const.CONTENT_RATING)?.let { contentRating ->
                        adManager.setContentRating(contentRating)
                        openMain.invoke(true)
                    } ?: run {
                        openOnboarding.invoke(true)
                    }
                    delay(500)
                    isAnimationEnabled = false
                }
            }
        }
    }
}