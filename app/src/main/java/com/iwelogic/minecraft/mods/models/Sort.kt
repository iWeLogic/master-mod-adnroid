package com.iwelogic.minecraft.mods.models

import com.iwelogic.minecraft.mods.R

enum class Sort(val id: String, val menuId: Int) {
    DOWNLOADS("downloads", R.id.sort_downloads),
    LIKES("likes", R.id.sort_likes),
    FILE_SIZE_DESC("file_desc", R.id.sort_file_desc),
    FILE_SIZE_ASC("file_asc", R.id.sort_file_asc),
    RANDOM("random", R.id.random);

    companion object {
        fun findByMenuId(id: Int) = values().firstOrNull { it.menuId == id } ?: RANDOM
    }
}