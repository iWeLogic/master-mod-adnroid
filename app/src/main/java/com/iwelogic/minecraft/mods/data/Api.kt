package com.iwelogic.minecraft.mods.data

import android.util.ArrayMap
import com.iwelogic.minecraft.mods.models.BaseItem
import com.iwelogic.minecraft.mods.models.BaseResponse
import com.iwelogic.minecraft.mods.models.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface Api {

    @GET("/api")
    suspend fun getBaseItems(@QueryMap fields: ArrayMap<String, Any>): Response<ResponseData<BaseItem>>

    @GET("/api?action=increase&value=installs")
    suspend fun increaseInstalls(@Query("type") type: String?, @Query("id") id: String?): Response<BaseResponse>

    @GET("/api?value=likes")
    suspend fun like(@Query("type") type: String?, @Query("id") id: String?, @Query("action") action: String?): Response<BaseResponse>
}