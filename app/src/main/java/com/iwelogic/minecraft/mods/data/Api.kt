package com.iwelogic.minecraft.mods.data

import com.iwelogic.minecraft.mods.models.BaseResponse
import com.iwelogic.minecraft.mods.models.Mod
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("/api/data/{category}")
    suspend fun getMods(@Path("category") category: String, @QueryMap fields: MultiMap<String, Any>): Response<List<Mod>>

    @PUT("/api/data/{category}")
    suspend fun updateMod(@Path("category") category: String?, @Body mod: Mod): Response<BaseResponse>
}