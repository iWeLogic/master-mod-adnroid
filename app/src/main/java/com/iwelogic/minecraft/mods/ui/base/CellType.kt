package com.iwelogic.minecraft.mods.ui.base

enum class CellType(val title: String, val type: Int, val fileExtension: String) {
    AD("ad", 3, ""),
    SKINS("skins", 2, "png"),
    ADDONS("addons", 1, "mcaddon"),
    TEXTURES("textures", 1, "mcpack"),
    SEEDS("seeds", 1, "mcworld"),
    MAPS("maps", 1, "mcworld"),
    BUILDINGS("buildings", 1, ""),
    PROGRESS("progress", 0, "");

    override fun toString(): String {
        return title
    }
}