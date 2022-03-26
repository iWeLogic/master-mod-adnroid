package com.iwelogic.minecraft.mods.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.MobileAds
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.data.Result
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.readString
import com.iwelogic.minecraft.mods.utils.writeBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : ViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)
    var openMain: SingleLiveEvent<Boolean> = SingleLiveEvent()
    var openOnboarding: SingleLiveEvent<Boolean> = SingleLiveEvent()

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
        loadAdd()
    }

    private fun loadAdd() {
        viewModelScope.launch {
            repository.getAdvertisingStatuses().collect { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.forEach {
                            if (it.id != null && it.status != null)
                                context.get()?.writeBoolean(it.id, it.status)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}