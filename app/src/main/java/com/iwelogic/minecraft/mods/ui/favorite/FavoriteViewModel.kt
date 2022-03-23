package com.iwelogic.minecraft.mods.ui.favorite

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : BaseViewModel(applicationContext) {

    val mods: MutableLiveData<MutableList<Mod>> = MutableLiveData(ArrayList())
    val openMod: SingleLiveEvent<Mod> = SingleLiveEvent()

    fun load() {
        repository.getFavouriteItems().observeForever {
            mods.postValue(it?.toMutableList())
        }
    }

    val onClick: (Mod) -> Unit = {
        openMod.invoke(it)
    }
}
