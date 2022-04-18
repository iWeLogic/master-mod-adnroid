package com.iwelogic.minecraft.mods.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Hash(
    @field:SerializedName("hash")
    @Expose
    var hash: String? = null
)