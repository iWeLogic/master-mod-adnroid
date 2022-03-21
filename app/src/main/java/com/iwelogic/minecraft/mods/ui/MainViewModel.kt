package com.iwelogic.minecraft.mods.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.iwelogic.minecraft.mods.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : ViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)


    fun checkAge() {
     /*   context.get()?.readString(Const.CONTENT_RATING)?.let {
            val requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setMaxAdContentRating(it)
                .setTestDeviceIds(listOf("5571260002C1C3A1FD32D49B3E5332C1", "81AC2F5CC6A169492DFD647D9F39B4AA"))
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            navigator?.openLoading()
        } ?: run {
            navigator?.openSelectAge()
        }*/
    }
}