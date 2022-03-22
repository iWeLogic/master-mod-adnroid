package com.iwelogic.minecraft.mods.models

import androidx.recyclerview.widget.DiffUtil
import com.iwelogic.minecraft.mods.R

enum class Sort(val id: String, val menuId: Int, val query: String) {
    DEFAULT("default", R.id.sort_default, ""),
    INSTALLS("default", R.id.sort_installs, "`installs` desc"),
    LIKES("default", R.id.sort_likes, "`likes` desc"),
    FILE_SIZE_DESC("default", R.id.sort_file_desc, "`fileSize` desc"),
    FILE_SIZE_ASC("default", R.id.sort_file_asc, "`fileSize` asc"),
    DATE("date", R.id.sort_date, "`addDate` desc");

    companion object {
        fun findByMenuId(id: Int) = values().firstOrNull { it.menuId == id } ?: DEFAULT
    }
}