package com.iwelogic.minecraft.mods.ui.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.ui.base_details.BaseDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(repository: Repository, @ApplicationContext applicationContext: Context) : BaseDetailsViewModel(repository, applicationContext) {

    val openInstall: SingleLiveEvent<File> = SingleLiveEvent()

    fun download() {
        item.value?.let { mod ->
            if (mod.progress != 0) return
            //    navigator?.showInterstitialAd()
            mod.progress = 1
            val dir = File("$base/${mod.category}/${mod.id}").apply { mkdirs() }
            Log.w("myLog", "download: " + mod.getFile())
            AndroidNetworking.download(mod.getFile(), dir.path, "file.${mod.getFileExtension()}")
                .setTag("Downloading file")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener { bytesDownloaded, totalBytes ->
                    if (totalBytes == 0L) {
                        mod.progress = 1
                    } else {
                        mod.progress = ((bytesDownloaded * 100 / ((mod.fileSize ?: 0.0) * 1024 * 1024).toInt()) * 100).toInt() + 1
                    }
                }
                .startDownload(object : DownloadListener {
                    override fun onDownloadComplete() {
                        viewModelScope.launch {
                            mod.installs = (mod.installs ?: 0) + 1
                            repository.updateMod(mod.category ?: "", mod).collect()
                        }
                        mod.progress = 10000
                    }

                    override fun onError(error: ANError?) {
                        dir.delete()
                        mod.progress = 0
                        //          navigator?.openNoConnection()
                    }
                })
        }

    }

    fun onClickInstall() {
       openInstall.invoke(File("$base/${item.value?.category}/${item.value?.id}/file.${item.value?.getFileExtension()}"))
    }

    override fun getFile() = File("$base/${item.value?.category}/${item.value?.id}/file.${item.value?.getFileExtension()}")
}
