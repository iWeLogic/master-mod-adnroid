package com.iwelogic.minecraft.mods.ui.favorite

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.Advertisement
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.fromPxToDp
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.readBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val message: MutableLiveData<String> = MutableLiveData("")
    val spanCount: MutableLiveData<Int> = MutableLiveData(1)

    fun load() {
        repository.getFavouriteItems().observeForever {
            if (it.isEmpty()) {
                message.postValue(context.get()?.getString(R.string.favorite_is_empty))
            } else {
                message.postValue(null)
            }
            mods.postValue(it?.toMutableList())
        }
    }

    val onClick: (Mod) -> Unit = {
        if (context.get()?.readBoolean(Advertisement.INTERSTITIAL_OPEN_DETAILS.id).isTrue()) {
            showInterstitial.invoke {
                context.get()?.let { FirebaseAnalytics.getInstance(it).logEvent("INTERSTITIAL_OPEN_DETAILS", Bundle()) }
                openMod.invoke(it)
            }
        } else {
            openMod.invoke(it)
        }
    }

    fun reloadScreenSize(widthDp: Int?) {
        widthDp?.fromPxToDp(context.get())?.let {
            spanCount.postValue(if (it > 680) 2 else 1)
        }
    }
}
