package com.iwelogic.minecraft.mods.models

import com.google.gson.annotations.SerializedName

data class ResponseData<T>(

    @field:SerializedName("data")
    val data: List<T>? = null
) : BaseResponse()


