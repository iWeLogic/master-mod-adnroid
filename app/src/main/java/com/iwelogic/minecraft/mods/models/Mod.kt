package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import android.util.Log
import androidx.annotation.NonNull
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.iwelogic.minecraft.mods.BuildConfig
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mod(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "primary_id")
    var primaryId: String = "",

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("countImages")
    val countImages: Int? = null,

    @field:SerializedName("fileSize")
    val fileSize: Double? = null,

    @field:SerializedName("priority")
    val priority: Int? = null,

    @field:SerializedName("pack")
    val pack: Int? = null,

    @field:SerializedName("version")
    val version: String? = null,

    @field:SerializedName("addDate")
    val addDate: Int? = null,

    @field:SerializedName("installs")
    var _installs: Long? = null,

    @field:SerializedName("likes")
    var _likes: Long? = null,

    var category: String? = null,

    @Transient
    @Ignore
    var _progress: Int = 0,

    @Transient
    @Ignore
    var downloaded: Boolean = false,

    @Transient
    var _progressGallery: Int = 0,

    @ColumnInfo(name = "favouriteDate")
    var favouriteDate: Long = 0

) : BaseObservable(), Parcelable {

    var progress: Int
        @Bindable get() = _progress
        set(value) {
            _progress = value
            notifyPropertyChanged(BR.progress)
        }
    var installs: Long?
        @Bindable get() = _installs
        set(value) {
            _installs = value
            notifyPropertyChanged(BR.installs)
        }

    var likes: Long?
        @Bindable get() = _likes
        set(value) {
            _likes = value
            notifyPropertyChanged(BR.likes)
        }

    var progressGallery: Int
        @Bindable get() = _progressGallery
        set(value) {
            _progressGallery = value
            notifyPropertyChanged(BR.progressGallery)
        }

    fun getFormattedLikes(): String {
        return convertBigNumbers(likes)
    }

    fun getFormattedDownloads(): String {
        return convertBigNumbers(installs)
    }

    private fun convertBigNumbers(value: Long?): String {
        value?.let {
            return when (it) {
                in 0L..999L -> it.toString()
                in 1000L..99999L -> "${String.format("%.1f", it.toFloat() / 1000).replace(".0", "")} k"
                in 100000L..999999L -> "${it / 1000} k"
                else -> "${String.format("%.1f", it.toFloat() / 100000).replace(".0", "")} m"
            }
        } ?: run {
            return ""
        }
    }

    fun generateVersion(): String {
        return try {
            val i2 = version!!.toInt() / 1000
            i2.toString() + "." + (version.toInt() - i2 * 1000)
        } catch (e: Exception) {
            ""
        }
    }

    fun getFileExtension() = when (category) {
        "maps" -> "mcworld"
        "addons" -> "mcaddon"
        "seeds" -> "mcworld"
        else -> "mcpack"
    }

    fun getFirstImage() = BuildConfig.BACKEND_FILES + "/" + category + "/" + id + "/images/0.jpg"

    fun getImages(): List<String> = (0 until (countImages ?: 0)).map {
        val image = BuildConfig.BACKEND_FILES + "/" + category + "/" + id + "/images/$it.jpg"
        Log.w("myLog", "getImages: " + image)
        image
    }

    fun getFile() = BuildConfig.BACKEND_FILES + "/" + category + "/" + id + "/file.${getFileExtension()}"
}
