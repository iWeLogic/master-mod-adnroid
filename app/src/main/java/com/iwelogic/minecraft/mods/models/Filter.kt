package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.iwelogic.minecraft.mods.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Filter(val id: String, val title: Int, val category: String) : Parcelable {

    ADDONS_O("0", R.string.addons_0, "addons"),
    ADDONS_1("1", R.string.addons_1, "addons"),
    ADDONS_2("2", R.string.addons_2, "addons"),
    ADDONS_3("3", R.string.addons_3, "addons"),
    ADDONS_4("4", R.string.addons_4, "addons"),
    ADDONS_5("5", R.string.addons_5, "addons"),
    ADDONS_6("6", R.string.addons_6, "addons"),
    ADDONS_7("7", R.string.addons_7, "addons"),
    ADDONS_8("8", R.string.addons_8, "addons"),

    MAPS_0("0", R.string.maps_0, "maps"),
    MAPS_1("1", R.string.maps_1, "maps"),
    MAPS_2("2", R.string.maps_2, "maps"),
    MAPS_3("3", R.string.maps_3, "maps"),
    MAPS_4("4", R.string.maps_4, "maps"),
    MAPS_5("5", R.string.maps_5, "maps"),
    MAPS_6("6", R.string.maps_6, "maps"),
    MAPS_7("7", R.string.maps_7, "maps"),

    TEXTURES_0("0", R.string.textures_0, "textures"),
    TEXTURES_1("1", R.string.textures_1, "textures"),
    TEXTURES_2("2", R.string.textures_2, "textures"),

    SEEDS_0("0", R.string.seeds_0, "seeds"),
    SEEDS_1("1", R.string.seeds_1, "seeds"),
    SEEDS_2("2", R.string.seeds_2, "seeds"),
    SEEDS_3("3", R.string.seeds_3, "seeds"),
    SEEDS_4("4", R.string.seeds_4, "seeds"),

    BUILDINGS_0("0", R.string.buildings_0, "buildings"),
    BUILDINGS_1("1", R.string.buildings_1, "buildings"),
    BUILDINGS_2("2", R.string.buildings_2, "buildings"),
    BUILDINGS_3("3", R.string.buildings_3, "buildings"),
    BUILDINGS_4("4", R.string.buildings_4, "buildings"),
    BUILDINGS_5("5", R.string.buildings_5, "buildings"),
    BUILDINGS_6("6", R.string.buildings_6, "buildings"),
    BUILDINGS_7("7", R.string.buildings_7, "buildings"),
    BUILDINGS_8("8", R.string.buildings_8, "buildings"),
    BUILDINGS_9("9", R.string.buildings_9, "buildings"),
    BUILDINGS_10("10", R.string.buildings_10, "buildings"),

    SKINS_0("0", R.string.skins_0, "skins"),
    SKINS_1("1", R.string.skins_1, "skins"),
    SKINS_2("2", R.string.skins_2, "skins"),
    SKINS_3("3", R.string.skins_3, "skins"),
    SKINS_4("4", R.string.skins_4, "skins"),
    SKINS_5("5", R.string.skins_5, "skins"),
    SKINS_6("6", R.string.skins_6, "skins"),
    SKINS_7("7", R.string.skins_7, "skins"),
    SKINS_8("8", R.string.skins_8, "skins"),
    SKINS_9("9", R.string.skins_9, "skins"),
    SKINS_10("10", R.string.skins_10, "skins"),
    SKINS_11("11", R.string.skins_11, "skins");

    companion object {
        fun getFiltersByCategory(category: String) = values().filter { it.category == category }

        fun getQuery(filters: List<FilterValue>?): String {
            var query = ""
            filters?.filter { it.value }?.forEach {
                query += if (query.isNotEmpty()) " OR " else ""
                query += "pack=${it.filter.id}"
            }
            return query
        }
    }
}

@Parcelize
data class FilterValue(var filter: Filter, var _value: Boolean) : BaseObservable(), Parcelable {

    @IgnoredOnParcel
    var valueChangeObserver: (() -> Unit)? = null

    var value: Boolean
        @Bindable get() = _value
        set(value) {
            valueChangeObserver?.invoke()
            _value = value
            notifyPropertyChanged(BR.value)
        }
}