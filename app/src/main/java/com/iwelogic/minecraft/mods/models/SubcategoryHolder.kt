package com.iwelogic.minecraft.mods.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import com.iwelogic.minecraft.mods.models.BaseItem
import kotlinx.coroutines.Job

data class SubcategoryHolder(
    var pack: String = "0",
    var page: Int = 0,
    var job: Job? = null,
    var title: String = "",
    val items: MutableList<BaseItem> = ArrayList(),
    var onScroll: ((Int) -> Unit)? = null,
    var onClick: ((BaseItem) -> Unit)? = null,
    var state: Parcelable? = null
) : BaseObservable()