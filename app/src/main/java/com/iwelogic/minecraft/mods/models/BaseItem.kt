package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "items")
@Parcelize
data class BaseItem(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "primary_id")
    var primaryId: String = "",

    @field:SerializedName("id")
    @ColumnInfo(name = "id")
    var id: String? = null,

    @field:SerializedName("date")
    @ColumnInfo(name = "date")
    var date: String? = null,

    @field:SerializedName("countImages")
    @ColumnInfo(name = "countImages")
    var countImages: Int? = null,

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String? = null,

    @field:SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String? = null,

    @field:SerializedName("filesize")
    @ColumnInfo(name = "filesize")
    var filesize: String? = null,

    @field:SerializedName("priority")
    @ColumnInfo(name = "priority")
    var priority: String? = null,

    @field:SerializedName("installs")
    @ColumnInfo(name = "installs")
    var install: Long? = null,

    @field:SerializedName("likes")
    @ColumnInfo(name = "likes")
    var like: Long? = null,

    @field:SerializedName("versionMine")
    @ColumnInfo(name = "versionMine")
    var versionMine: String? = null,

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

) : BasePack(), Parcelable {

    var progress: Int
        @Bindable get() = _progress
        set(value) {
            _progress = value
            notifyPropertyChanged(BR.progress)
        }
    var installs: Long?
        @Bindable get() = install
        set(value) {
            install = value
            notifyPropertyChanged(BR.installs)
        }

    var likes: Long?
        @Bindable get() = like
        set(value) {
            like = value
            notifyPropertyChanged(BR.likes)
        }

    var progressGallery: Int
        @Bindable get() = _progressGallery
        set(value) {
            _progressGallery = value
            notifyPropertyChanged(BR.progressGallery)
        }

    fun getDownloadFileExtension() = when (category) {
        "maps" -> "mcworld"
        else -> "zip"
    }

    fun getFileExtension() = when (category) {
        "maps" -> "mcworld"
        "addons" -> "mcaddon"
        "seeds" -> "mcworld"
        else -> "mcpack"
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

    fun getVersion(): String {
        return try {
            val i2 = versionMine!!.toInt() / 1000
            i2.toString() + "." + (versionMine!!.toInt() - i2 * 1000)
        } catch (e: Exception) {
            ""
        }
    }
}