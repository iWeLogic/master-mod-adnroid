package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import com.iwelogic.minecraft.mods.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Type(val id: String, val title: Int, val cellType: Int, val fileExtension: String, val sortMenu: Int, val spanCount: Int) : Parcelable {

    AD("ad", 0, 3, "", 0, 1),

    PROGRESS("progress", 0, 0, "", 0, 1),

    SKINS("skins", R.string.skins, 2, "png", R.menu.sort_skins, 2),

    ADDONS("addons", R.string.addons, 1, "mcaddon", R.menu.sort, 1),

    TEXTURES("textures", R.string.textures, 1, "mcpack", R.menu.sort, 1),

    SEEDS("seeds", R.string.seeds, 1, "mcworld", R.menu.sort, 1),

    MAPS("maps", R.string.maps, 1, "mcworld", R.menu.sort, 1),

    BUILDINGS("buildings", 0, 1, "", R.menu.sort, 1);

    override fun toString(): String {
        return id
    }

    companion object {
        fun getValueById(id: String) = values().firstOrNull { it.id == id } ?: ADDONS
    }
}