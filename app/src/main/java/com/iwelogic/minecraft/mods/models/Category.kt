package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import com.iwelogic.minecraft.mods.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Category(val id: String, val title: Int, val icon: Int, val subcategories: List<Subcategory>) : Parcelable {

    ADDONS(
        "addons", R.string.addons, R.drawable.addons, listOf(
            Subcategory("0", R.string.addons_0),
            Subcategory("1", R.string.addons_1),
            Subcategory("2", R.string.addons_2),
            Subcategory("3", R.string.addons_3),
            Subcategory("4", R.string.addons_4),
            Subcategory("5", R.string.addons_5),
            Subcategory("6", R.string.addons_6),
            Subcategory("7", R.string.addons_7),
            Subcategory("8", R.string.addons_8)
        )
    ),
    MAPS(
        "maps", R.string.maps, R.drawable.maps, listOf(
            Subcategory("0", R.string.maps_0),
            Subcategory("1", R.string.maps_1),
            Subcategory("2", R.string.maps_2),
            Subcategory("3", R.string.maps_3),
            Subcategory("4", R.string.maps_4),
            Subcategory("5", R.string.maps_5),
            Subcategory("6", R.string.maps_6),
            Subcategory("7", R.string.maps_7)
        )
    ),
    TEXTURES(
        "textures", R.string.textures, R.drawable.textures, listOf(
            Subcategory("0", R.string.textures_0),
            Subcategory("1", R.string.textures_1),
            Subcategory("2", R.string.textures_2)
        )
    ),
    SEEDS(
        "seeds", R.string.seeds, R.drawable.seeds, listOf(
            Subcategory("0", R.string.seeds_0),
            Subcategory("1", R.string.seeds_1),
            Subcategory("2", R.string.seeds_2),
            Subcategory("3", R.string.seeds_3),
            Subcategory("4", R.string.seeds_4)
        )
    ),
    BUILDINGS(
        "buildings", R.string.buildings, R.drawable.buildings, listOf(
            Subcategory("0", R.string.buildings_0),
            Subcategory("1", R.string.buildings_1),
            Subcategory("2", R.string.buildings_2),
            Subcategory("3", R.string.buildings_3),
            Subcategory("4", R.string.buildings_4),
            Subcategory("5", R.string.buildings_5),
            Subcategory("6", R.string.buildings_6),
            Subcategory("7", R.string.buildings_7),
            Subcategory("8", R.string.buildings_8),
            Subcategory("9", R.string.buildings_9),
            Subcategory("10", R.string.buildings_10)
        )
    ),
    SKINS(
        "skins", R.string.skins, R.drawable.skins, listOf(
            Subcategory("0", R.string.skins_0),
            Subcategory("1", R.string.skins_1),
            Subcategory("2", R.string.skins_2),
            Subcategory("3", R.string.skins_3),
            Subcategory("4", R.string.skins_4),
            Subcategory("5", R.string.skins_5),
            Subcategory("6", R.string.skins_6),
            Subcategory("7", R.string.skins_7),
            Subcategory("8", R.string.skins_8),
            Subcategory("9", R.string.skins_9),
            Subcategory("10", R.string.skins_10),
            Subcategory("11", R.string.skins_11)
        )
    );

    companion object {
        fun getByIndex(index: Int) = values().filter { it.id != "buildings" }[index]
        fun getById(id: String) = values().firstOrNull { it.id == id }
    }
}

@Parcelize
class Subcategory(
    var id: String,
    var title: Int
) : Parcelable