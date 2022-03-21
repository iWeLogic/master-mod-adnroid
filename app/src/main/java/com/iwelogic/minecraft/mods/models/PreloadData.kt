package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PreloadData(

    var items: MutableList<BaseListItemData> = ArrayList()
) : Parcelable

@Parcelize
class BaseListItemData(
    var pack: String = "0",
    val items: List<BaseItem>? = null,
) : Parcelable