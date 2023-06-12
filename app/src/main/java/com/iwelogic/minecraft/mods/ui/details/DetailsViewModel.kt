package com.iwelogic.minecraft.mods.ui.details

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.DialogData
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.ui.base_details.BaseDetailsViewModel
import com.iwelogic.minecraft.mods.ui.main.MainViewModel
import com.iwelogic.minecraft.mods.utils.readInteger
import com.iwelogic.minecraft.mods.utils.writeBoolean
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
            mod.progress = 1
            val dir = File("$base/${mod.type}/${mod.id}").apply { mkdirs() }
            AndroidNetworking.download(mod.getFile(), dir.path, "file.${mod.type?.fileExtension}")
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
                        }
                        mod.progress = 10000
                    }

                    override fun onError(error: ANError?) {
                        dir.delete()
                        mod.progress = 0
                        showDialog.invoke(
                            DialogData(
                                title = context.get()?.getString(R.string.no_internet_connection),
                                message = context.get()?.getString(R.string.no_internet_connection_text),
                                buttonRightTitle = context.get()?.getString(R.string.ok)
                            )
                        )
                    }
                })
        }

    }

    fun onClickInstall() {
        context.get()?.writeBoolean(MainViewModel.INSTALLED, true)
        openInstall.invoke(File("$base/${item.value?.type}/${item.value?.id}/file.${item.value?.type?.fileExtension}"))
    }

    override fun getFile() = File("$base/${item.value?.type}/${item.value?.id}/file.${item.value?.type?.fileExtension}")
}
