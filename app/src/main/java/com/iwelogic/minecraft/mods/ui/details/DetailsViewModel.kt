package com.iwelogic.minecraft.mods.ui.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.ui.base.BaseViewModel
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.utils.isTrue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : BaseViewModel() {

    val item: MutableLiveData<Mod> = MutableLiveData()
    var isFavourite: LiveData<Boolean>? = null
    val base = "${applicationContext.filesDir?.path}"
    val openHelp: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun checkIsFileExist() {
        val file = File("$base/${item.value?.category}/${item.value?.id}/file.${item.value?.getFileExtension()}")
        item.value?.progress = if (file.exists()) 10000 else 0
        isFavourite = repository.checkExist("${item.value?.category} ${item.value?.pack} ${item.value?.id}")
    }

    override fun onCleared() {
        AndroidNetworking.cancelAll()
        super.onCleared()
    }

    fun download() {
        if (item.value?.progress != 0) return
        //    navigator?.showInterstitialAd()
        item.value?.progress = 1
        val dir = File("$base/${item.value?.category}/${item.value?.id}").apply { mkdirs() }
        AndroidNetworking.download(item.value?.getFile(), dir.path, "file.${item.value?.getFileExtension()}")
            .setTag("Downloading file")
            .setPriority(Priority.MEDIUM)
            .build()
            .setDownloadProgressListener { bytesDownloaded, totalBytes ->
                if (totalBytes == 0L) {
                    item.value?.progress = 1
                } else {
                    item.value?.progress = ((bytesDownloaded * 100 / totalBytes) * 100).toInt() + 1
                }
            }
            .startDownload(object : DownloadListener {
                override fun onDownloadComplete() {
                    viewModelScope.launch {
                        //     repository.increaseInstalls(item.value?.category, item.value?.id).collect()
                        item.value?.installs = (item.value?.installs ?: 0) + 1
                    }
                    item.value?.progress = 10000
                }

                override fun onError(error: ANError?) {
                    dir.delete()
                    item.value?.progress = 0
                    //          navigator?.openNoConnection()
                }
            })
    }

    fun onClickInstall() {
        //   navigator?.install(File("$base/${item.value?.category}/${item.value?.id}/file.${item.value?.getFileExtension()}").path)
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
}
