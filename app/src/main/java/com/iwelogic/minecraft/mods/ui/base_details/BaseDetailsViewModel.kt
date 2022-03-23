package com.iwelogic.minecraft.mods.ui.base_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.readBoolean
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.lang.ref.WeakReference

abstract class BaseDetailsViewModel(val repository: Repository, applicationContext: Context) : BaseViewModel() {

    var isFavourite: LiveData<Boolean>? = null
    val openHelp: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val base = "${applicationContext.filesDir?.path}"
    val item: MutableLiveData<Mod> = MutableLiveData()
    var context: WeakReference<Context> = WeakReference(applicationContext)

    fun checkIsFileExist() {
        val file = File("$base/${item.value?.category}/${item.value?.id}/file.${item.value?.getFileExtension()}")
        item.value?.progress = if (file.exists()) 10000 else 0
        isFavourite = repository.checkExist("${item.value?.category} ${item.value?.pack} ${item.value?.id}")
        item.value?.progressGallery = if (context.get()?.readBoolean("${item.value?.category}_${item.value?.id}").isTrue()) 10000 else 0
        isFavourite = repository.checkExist("${item.value?.category} ${item.value?.pack} ${item.value?.id}")
    }

    fun onClickFavourite() {
        item.value?.let { mod ->
            mod.primaryId = "${mod.category} ${mod.pack} ${mod.id}"
            viewModelScope.launch {
                if (isFavourite?.value.isTrue()) {
                    repository.removeFromFavourite(mod).collect()
                    mod.likes = mod.likes?.minus(1)
                    repository.updateMod(mod.category ?: "", mod).collect()
                } else {
                    mod.favouriteDate = System.currentTimeMillis()
                    repository.setFavourite(mod).collect()
                    mod.likes = mod.likes?.plus(1)
                    repository.updateMod(mod.category ?: "", mod).collect()
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