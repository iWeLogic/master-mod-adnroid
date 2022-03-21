package com.iwelogic.minecraft.mods.models

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

open class BasePack : BaseObservable() {
    @field:SerializedName("pack")
    var pack: String? = null

    var category: String = "addons"
}