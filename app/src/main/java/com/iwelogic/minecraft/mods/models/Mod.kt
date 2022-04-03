package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.*
import androidx.room.TypeConverter
import com.google.android.gms.ads.AdView
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.iwelogic.minecraft.mods.BuildConfig
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "mods")
@Parcelize
data class Mod(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "primary_id")
    var primaryId: String = "",

    @field:SerializedName("id")
    @Expose
    var id: Int? = null,

    @field:SerializedName("title")
    @Expose
    var title: String? = null,

    @field:SerializedName("objectId")
    @Expose
    var objectId: String? = null,

    @field:SerializedName("description")
    @Expose
    var description: String? = null,

    @field:SerializedName("countImages")
    @Expose
    var countImages: Int? = null,

    @field:SerializedName("fileSize")
    @Expose
    var fileSize: Double? = null,

    @field:SerializedName("priority")
    @Expose
    var priority: Int? = null,

    @field:SerializedName("pack")
    @Expose
    var pack: Int? = null,

    @field:SerializedName("version")
    @Expose
    var version: String? = null,

    @field:SerializedName("addDate")
    @Expose
    var addDate: Int? = null,

    @field:SerializedName("installs")
    @ColumnInfo(name = "installs")
    @Expose
    var p_installs: Long? = null,

    @field:SerializedName("likes")
    @ColumnInfo(name = "likes")
    @Expose
    var p_likes: Long? = null,

    @ColumnInfo(name = "type")
    var type: Type? = null,

    var category: String? = null,

    @Transient
    @Ignore
    var _progress: Int = 0,

    @Transient
    var _progressGallery: Int = 0,

    @ColumnInfo(name = "favouriteDate")
    var favouriteDate: Long = 0

) : BaseObservable(), Parcelable {

    @Transient
    @Ignore
    @IgnoredOnParcel
    var adView: AdView? = null

    var progress: Int
        @Bindable get() = _progress
        set(value) {
            _progress = value
            notifyPropertyChanged(BR.progress)
        }
    var installs: Long?
        @Bindable get() = p_installs
        set(value) {
            p_installs = value
            notifyPropertyChanged(BR.installs)
        }

    var likes: Long?
        @Bindable get() = p_likes
        set(value) {
            p_likes = value
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
            i2.toString() + "." + (version!!.toInt() - i2 * 1000)
        } catch (e: Exception) {
            ""
        }
    }

    fun getImage() = BuildConfig.BACKEND_FILES + "/" + type + "/" + id + "/images/0.${if (type?.id == "skins") "png" else "jpg"}"


    fun getImages(): List<String> = (0 until (countImages ?: 0)).map {
        val image = BuildConfig.BACKEND_FILES + "/" + type + "/" + id + "/images/$it.jpg"
        //  Log.w("myLog", "getImages: " + image)
        image
    }

    fun getFile() = BuildConfig.BACKEND_FILES + "/" + type + "/" + id + "/file.${type?.fileExtension}"

}

// TypeConverter
class TypeConverter {

    @androidx.room.TypeConverter
    fun toString(type: Type?): String? {
        return type?.id
    }

    @TypeConverter
    fun toType(value: String?): Type? {
        return Type.getValueById(value ?: "")
    }
}
