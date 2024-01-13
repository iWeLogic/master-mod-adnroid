package com.iwelogic.minecraft.mods.ui.base_details

import android.content.Context
import androidx.lifecycle.*
import com.androidnetworking.AndroidNetworking
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.manager.*
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.*
import com.iwelogic.minecraft.mods.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

abstract class BaseDetailsViewModel(val repository: Repository, private val firebaseConfigManager: FirebaseConfigManager, applicationContext: Context) :
    BaseViewModel(applicationContext) {

    var isFavourite: LiveData<Boolean>? = null
    val openHelp: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val reloadAd: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val base = "${applicationContext.filesDir?.path}"
    val item: MutableLiveData<Mod> = MutableLiveData()

    abstract fun getFile(): File

    fun checkIsFileExist() {
        item.value?.progress = if (getFile().exists()) 10000 else 0
        isFavourite =
            repository.checkExist("${item.value?.type} ${item.value?.pack} ${item.value?.id}")
    }

    fun onClickFavourite() {
        item.value?.let { mod ->
            mod.primaryId = "${mod.type} ${mod.pack} ${mod.id}"
            viewModelScope.launch {
                if (isFavourite?.value.isTrue()) {
                    repository.removeFromFavourite(mod).collect()
                    mod.likes = mod.likes?.minus(1)
                } else {
                    mod.favouriteDate = System.currentTimeMillis()
                    mod.likes = mod.likes?.plus(1)
                    repository.setFavourite(mod).collect()
                }
            }
        }
    }

    fun onClickHelp() {
        openHelp.invoke(true)
    }

    override fun onCleared() {
        AndroidNetworking.cancelAll()
        super.onCleared()
    }

    fun reloadAd() {
        if (firebaseConfigManager.getAdUnitStatus(AdUnit.NATIVE_ON_DETAILS)) {
            reloadAd.invoke(true)
        }
    }
}