package com.iwelogic.minecraft.mods.data

import com.iwelogic.minecraft.mods.models.Mod
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("/api/data/{category}")
    suspend fun getMods(@Path("category") category: String, @QueryMap fields: MultiMap<String, Any>): Response<List<Mod>>
}