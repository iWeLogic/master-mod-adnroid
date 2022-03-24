package com.iwelogic.minecraft.mods.models

import com.google.gson.annotations.SerializedName

data class AdvertisingStatus(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
