package com.iwelogic.minecraft.mods.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdvertisingStatus(

    @field:SerializedName("id")
    @Expose
    val id: String? = null,

    @field:SerializedName("status")
    @Expose
    val status: Boolean? = null
)
