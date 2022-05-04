package com.iwelogic.minecraft.mods.ui.base_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.Const
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.readString
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

abstract class BaseDetailsViewModel(val repository: Repository, applicationContext: Context) : BaseViewModel(applicationContext) {

    var isFavourite: LiveData<Boolean>? = null
    var selectedAge: String = applicationContext.readString(Const.AGE) ?: ""
    var selectedContentRating: String = applicationContext.readString(Const.CONTENT_RATING) ?: ""
    val openHelp: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val base = "${applicationContext.filesDir?.path}"
    val item: MutableLiveData<Mod> = MutableLiveData()

    abstract fun getFile(): File

    fun checkIsFileExist() {
        item.value?.progress = if (getFile().exists()) 10000 else 0
        isFavourite = repository.checkExist("${item.value?.type} ${item.value?.pack} ${item.value?.id}")
    }

    fun onClickFavourite() {
        item.value?.let { mod ->
            mod.primaryId = "${mod.type} ${mod.pack} ${mod.id}"
            viewModelScope.launch {
                if (isFavourite?.value.isTrue()) {
                    repository.removeFromFavourite(mod).collect()
                    mod.likes = mod.likes?.minus(1)
                    repository.updateMod(mod.type?.id ?: "", mod).collect()
                } else {
                    mod.favouriteDate = System.currentTimeMillis()
                    mod.likes = mod.likes?.plus(1)
                    repository.setFavourite(mod).collect()
                    repository.updateMod(mod.type?.id ?: "", mod).collect()
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
}