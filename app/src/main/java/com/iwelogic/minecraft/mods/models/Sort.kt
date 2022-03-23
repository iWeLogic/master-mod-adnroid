package com.iwelogic.minecraft.mods.models

import com.iwelogic.minecraft.mods.R

enum class Sort(val id: String, val menuId: Int, val query: String) {
    DOWNLOADS("downloads", R.id.sort_downloads, "`installs` desc"),
    LIKES("likes", R.id.sort_likes, "`likes` desc"),
    FILE_SIZE_DESC("file_desc", R.id.sort_file_desc, "`fileSize` desc"),
    FILE_SIZE_ASC("file_asc", R.id.sort_file_asc, "`fileSize` asc"),
    DATE("date", R.id.sort_date, "`addDate` desc");

    companion object {
        fun findByMenuId(id: Int) = values().firstOrNull { it.menuId == id } ?: DATE
    }
}