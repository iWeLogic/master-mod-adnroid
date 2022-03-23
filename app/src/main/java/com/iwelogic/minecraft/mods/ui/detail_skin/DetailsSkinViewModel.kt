package com.iwelogic.minecraft.mods.ui.detail_skin

import android.content.*
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.SingleLiveEvent
import com.iwelogic.minecraft.mods.ui.base_details.BaseDetailsViewModel
import com.iwelogic.minecraft.mods.utils.writeBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.zeroturnaround.zip.ZipUtil
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailsSkinViewModel @Inject constructor(repository: Repository, @ApplicationContext applicationContext: Context) : BaseDetailsViewModel(repository, applicationContext) {

    val openInstall: SingleLiveEvent<String> = SingleLiveEvent()
    val openIntent: SingleLiveEvent<Intent> = SingleLiveEvent()
    val openCheckPermission: SingleLiveEvent<() -> Unit> = SingleLiveEvent()

    fun onClickDownloadToGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadImageToGalley()
        } else {
            openCheckPermission.invoke {
                downloadImageToGalley()
            }
        }
    }

    private fun downloadImageToGalley() {
        showInterstitial.invoke(null)
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                //downloading image
                item.value?.let { mod ->
                    mod.progressGallery = 100
                    val connection: HttpURLConnection = URL(mod.getFile()).openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input: InputStream = connection.inputStream
                    val image = BitmapFactory.decodeStream(input)
                    mod.progressGallery = 5000
                    val name = "skin_${mod.id}"
                    //saving image
                    val status = saveImage(image, "$name.png")
                    if (status) {
                        context.get()?.writeBoolean(name, true)
                        mod.progressGallery = 10000
                        mod.installs = mod.installs?.plus(1)
                        repository.updateMod(mod.category ?: "", mod).collect()
                    } else {
                        mod.progressGallery = 0
                    }
                }
            }.onFailure {
                showDialog.invoke(
                    DialogData(
                        title = context.get()?.getString(R.string.no_internet_connection),
                        message = context.get()?.getString(R.string.no_internet_connection_text),
                        buttonRightTitle = context.get()?.getString(R.string.ok)
                    )
                )
                item.value?.progressGallery = 0
            }
        }
    }

    fun onClickDownloadToMinecraft() {
        showInterstitial.invoke(null)
        item.value?.let { mod ->
            viewModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    //downloading image
                    mod.progress = 100
                    val connection: HttpURLConnection = URL(mod.getFile()).openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input: InputStream = connection.inputStream
                    val image = BitmapFactory.decodeStream(input)
                    mod.progress = 3000

                    //creating files
                    val name = "skin_${mod.id}"

                    val dir = File("$base/${item.value?.category}/${mod.id}/$name").apply { mkdirs() }
                    val fileSkins = File("${dir.absolutePath}/skins.json")
                    val fileManifest = File("${dir.absolutePath}/manifest.json")
                    val fileImage = File("${dir.absolutePath}/$name.png")
                    val filePack = File("$base/skins/$name.mcpack")
                    mod.progress = 4000

                    //writings data into skins.json
                    val skins = Skins().apply {
                        serializeName = name
                        localizationName = name
                        skins = listOf(SkinsItem().apply {
                            localizationName = name
                            geometry = "geometry.$name.$name"
                            texture = "$name.png"
                            type = "free"
                        })
                    }
                    writeFile(fileSkins, Gson().toJson(skins))
                    mod.progress = 6000

                    //writing data into manifest.json
                    val manifest = Manifest().apply {
                        formatVersion = 1
                        header = Header().apply {
                            this.name = name
                            uuid = UUID.randomUUID().toString()
                            version = listOf(1, 0, 0)
                        }
                        modules = listOf(ModulesItem().apply {
                            type = "skin_pack"
                            uuid = UUID.randomUUID().toString()
                            version = listOf(1, 0, 0)
                        })
                    }
                    writeFile(fileManifest, Gson().toJson(manifest))
                    mod.progress = 8000

                    //saving image
                    image.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(fileImage))

                    //compressing into mcpack
                    ZipUtil.pack(dir, filePack, true)
                    mod.progress = 10000
                    mod.installs = mod.installs?.plus(1)
                    repository.updateMod(mod.category ?: "", mod).collect()
                }.onFailure {
                    showDialog.invoke(
                        DialogData(
                            title = context.get()?.getString(R.string.no_internet_connection),
                            message = context.get()?.getString(R.string.no_internet_connection_text),
                            buttonRightTitle = context.get()?.getString(R.string.ok)
                        )
                    )
                    mod.progress = 0
                }
            }
        }
    }

    private fun writeFile(file: File, str: String) {
        val bufferedWriter = BufferedWriter(FileWriter(file))
        bufferedWriter.write(str)
        bufferedWriter.close()
    }

    private fun getMinecraftVersion(): Int {
        return try {
            val packageInfo: PackageInfo = context.get()?.packageManager?.getPackageInfo("com.mojang.minecraftpe", 0) ?: return 0
            val split = packageInfo.versionName.split("\\.".toRegex()).toTypedArray()
            split[0].toInt() * 1000 + split[1].toInt()
        } catch (e: Exception) {
            0
        }
    }

    @Suppress("DEPRECATION")
    private fun saveImage(bitmap: Bitmap, name: String): Boolean {
        var saved = false
        var fos: OutputStream? = null
        try {
            fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver: ContentResolver = context.get()?.contentResolver!!
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(imageUri!!)
            } else {
                val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                if (!file.exists()) {
                    file.mkdir()
                }
                val image = File(file, name)
                FileOutputStream(image)
            }
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos?.flush()
            fos?.close()
        } catch (e: java.lang.Exception) {

        } finally {
            fos?.close()
        }
        return saved
    }

    fun onClickInstall() {
        when (getMinecraftVersion()) {
            0 -> showDialogNeedInstallMinecraft()
            in 1012..9999 -> openInstall.invoke(File("$base/skins/skin_${item.value?.id}.mcpack").path)
            else -> showDialog.invoke(
                DialogData(
                    title = context.get()?.getString(R.string.install_skin_through_gallery_title),
                    message = context.get()?.getString(R.string.download_skin_to_gallery_body),
                    buttonRightTitle = context.get()?.getString(R.string.ok)
                )
            )
        }
    }

    fun showDialogNeedInstallMinecraft(){
        showDialog.invoke(
            DialogData(
                title = context.get()?.getString(R.string.minecraft_isnt_installed_title),
                message = context.get()?.getString(R.string.minecraft_isnt_installed_body),
                buttonRightTitle = context.get()?.getString(R.string.install),
                buttonLeftTitle = context.get()?.getString(R.string.no),
                onClickRight = {
                    try {
                        openIntent.invoke(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.mojang.minecraftpe")))
                    } catch (e: ActivityNotFoundException) {
                        openIntent.invoke(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mojang.minecraftpe")))
                    }
                }
            )
        )
    }

    override fun getFile() = File("$base/skins/skin_${item.value?.id}.mcpack")
}
