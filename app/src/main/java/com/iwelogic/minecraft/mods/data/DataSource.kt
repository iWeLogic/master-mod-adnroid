package com.iwelogic.minecraft.mods.data

import android.content.Context
import com.google.gson.Gson
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.models.BaseResponse
import com.iwelogic.minecraft.mods.models.Mod
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import java.lang.ref.WeakReference
import java.net.UnknownHostException
import javax.inject.Inject

class DataSource @Inject constructor(private val api: Api, @ApplicationContext applicationContext: Context) {

    var context: WeakReference<Context> = WeakReference(applicationContext)

    suspend fun getMods(queries: MultiMap<String, Any>): Result<List<Mod>> {
        return getResponse(request = { api.getMods(queries) })
    }

    suspend fun increaseInstalls(type: String?, id: String?): Result<BaseResponse> {
        return getResponse(request = { api.increaseInstalls(type, id) })
    }

    suspend fun like(type: String?, id: String?, action: String?): Result<BaseResponse> {
        return getResponse(request = { api.like(type, id, action) })
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun <T> getResponse(request: suspend () -> Response<T>): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                Result.Success(result.body())
            } else {
                return try {
                    val responseError = Gson().fromJson(result.errorBody()?.string(), BaseResponse::class.java)
                    Result.Error(/*responseError.code*/ null ?: Result.Error.Code.UNKNOWN, responseError.message)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.Error(Result.Error.Code.UNKNOWN, e.message)
                }
            }
        } catch (e: Throwable) {
            when (e) {
                is UnknownHostException -> Result.Error(Result.Error.Code.NO_CONNECTION, context.get()?.getString(R.string.something_went_wrong))
                else -> Result.Error(Result.Error.Code.UNKNOWN, e.message)
            }
        }
    }
}