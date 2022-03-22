package com.iwelogic.minecraft.mods.data

import android.util.ArrayMap
import com.iwelogic.minecraft.mods.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {

    @GET("/api/data/{category}")
    suspend fun getMods(@Path("category") category: String, @QueryMap fields: MultiMap<String, Any>): Response<List<Mod>>

    @GET("/api?action=increase&value=installs")
    suspend fun increaseInstalls(@Query("type") type: String?, @Query("id") id: String?): Response<BaseResponse>

    @GET("/api?value=likes")
    suspend fun like(@Query("type") type: String?, @Query("id") id: String?, @Query("action") action: String?): Response<BaseResponse>
}