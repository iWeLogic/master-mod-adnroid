package com.iwelogic.minecraft.mods.ui.detail_skin

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.iwelogic.minecraft.mods.BuildConfig
import com.iwelogic.minecraft.mods.data.Repository
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.ui.base.storage.StorageViewModel
import com.iwelogic.minecraft.mods.utils.isTrue
import com.iwelogic.minecraft.mods.utils.readBoolean
import com.iwelogic.minecraft.mods.utils.writeBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.zeroturnaround.zip.ZipUtil
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DetailsSkinViewModel @Inject constructor(private val repository: Repository, @ApplicationContext applicationContext: Context) : StorageViewModel() {

    var context: WeakReference<Context> = WeakReference(applicationContext)
    val item: MutableLiveData<Mod> = MutableLiveData()
    val base = "${applicationContext.filesDir?.path}"
    var isFavourite: LiveData<Boolean>? = null

    fun checkIsFileExist() {
        val name = "skin_n${item.value?.id}"
        val file = File("$base/skins/$name.mcpack")
        item.value?.progress = if (file.exists()) 10000 else 0
        item.value?.progressGallery = if (context.get()?.readBoolean(name).isTrue()) 10000 else 0
    //    isFavourite = repository.checkExist("${item.value?.category} ${item.value?.pack} ${item.value?.id}")
    }

    fun onClickDownloadToGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            downloadImageToGalley()
        } else {
          /*  navigator?.checkPermissionAction {
                downloadImageToGalley()
            }*/
        }
    }

    fun downloadImageToGalley() {
//        navigator?.showInterstitialAd()
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                //downloading image
                item.value?.progressGallery = 100
                val connection: HttpURLConnection = URL(item.value?.getFile()).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                val image = BitmapFactory.decodeStream(input)
                item.value?.progressGallery = 5000
                val name = "skin_n${item.value?.id}"

                //saving image
                val status = saveImage(image, "$name.png")
                if (status) {
                    context.get()?.writeBoolean(name, true)
                    item.value?.progressGallery = 10000
                   // repository.increaseInstalls("skins", item.value?.id).collect()
                    item.value?.installs = (item.value?.installs ?: 0) + 1
                } else {
                    item.value?.progressGallery = 0
                }
            }.onFailure {
               // navigator?.showNoConnectionDownloadGallery()
                item.value?.progressGallery = 0
            }
        }
    }

    fun downloadToMinecraft() {
      //  navigator?.showInterstitialAd()
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {

                //downloading image
                item.value?.progress = 100
                val connection: HttpURLConnection = URL(item.value?.getFile()).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                val image = BitmapFactory.decodeStream(input)
                item.value?.progress = 3000

                //creating files
                val name = "skin_n${item.value?.id}"
                val dir = File("$base/skins/${item.value?.id}/$name").apply { mkdirs() }
                val fileSkins = File("${dir.absolutePath}/skins.json")
                val fileManifest = File("${dir.absolutePath}/manifest.json")
                val fileImage = File("${dir.absolutePath}/$name.png")
                val filePack = File("$base/skins/$name.mcpack")
                item.value?.progress = 4000

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
                item.value?.progress = 6000

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
                item.value?.progress = 8000

                //saving image
                image.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(fileImage))

                //compressing into mcpack
                ZipUtil.pack(dir, filePack, true)
                item.value?.progress = 10000
             //   repository.increaseInstalls("skins", item.value?.id).collect()
                item.value?.installs = (item.value?.installs ?: 0) + 1
            }.onFailure {
              //  navigator?.showNoConnectionDownloadMinecraft()
                item.value?.progress = 0
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
                val image = File(file, "$name.png")
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
        when {
         /*   getMinecraftVersion() == 0 -> navigator?.showInstallMinecraft()
            getMinecraftVersion() > 1012 -> navigator?.install(File("$base/skins/skin_n${item.value?.id}.mcpack").path)
            else -> navigator?.showMessageDialog(context.get()?.getString(R.string.install_skin_through_gallery_title), context.get()?.getString(R.string.install_skin_through_gallery_body))
     */   }
    }

    fun onClickFavourite() {
        item.value?.let {
            it.primaryId = "${it.category} ${it.pack} ${it.id}"
            viewModelScope.launch {
                if (isFavourite?.value.isTrue()) {
              //      repository.removeFromFavourite(it).collect()
                    item.value?.likes = (item.value?.likes ?: 0) - 1
               //     repository.like(item.value?.category, item.value?.id, "decrease").collect()
                } else {
                    it.favouriteDate = System.currentTimeMillis()
               //     repository.setFavourite(it).collect()
                    item.value?.likes = (item.value?.likes ?: 0) + 1
               //     repository.like(item.value?.category, item.value?.id, "increase").collect()
                }
            }
        }
    }

    fun onClickHelp(){
     //   item.value?.category?.let { navigator?.openHelp(it) }
    }
}
