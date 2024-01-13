package com.iwelogic.minecraft.mods.ui.favorite

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.manager.AdUnit
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.*
import com.iwelogic.minecraft.mods.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()
    val message: MutableLiveData<String?> = MutableLiveData("")
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
        showInterstitial.invoke(
            AdDataHolder(
                adUnit = AdUnit.OPEN_DETAILS,
                callback = {
                    context.get().logEvent(AdUnit.OPEN_DETAILS.code)
                    openMod.invoke(it)
                }
            )
        )
    }

    fun reloadScreenSize(widthDp: Int?) {
        widthDp?.fromPxToDp(context.get())?.let {
            spanCount.postValue(if (it > 680) 2 else 1)
        }
    }
}
