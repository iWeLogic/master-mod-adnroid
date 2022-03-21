package com.iwelogic.minecraft.mods.data

import android.content.Context
import android.util.ArrayMap
import com.iwelogic.minecraft.mods.R
import com.iwelogic.minecraft.mods.models.BaseItem
import com.iwelogic.minecraft.mods.models.BaseResponse
import com.iwelogic.minecraft.mods.models.ResponseData
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class DataSource @Inject constructor(private val api: Api, @ApplicationContext applicationContext: Context) {

    var context: WeakReference<Context> = WeakReference(applicationContext)

    suspend fun getBaseItems(queries: ArrayMap<String, Any>): IResult<ResponseData<BaseItem>> {
        return getResponse(request = { api.getBaseItems(queries) }, defaultErrorMessage = context.get()?.getString(R.string.something_went_wrong))
    }

    suspend fun increaseInstalls(type: String?, id: String?): IResult<BaseResponse> {
        return getResponse(request = { api.increaseInstalls(type, id) }, defaultErrorMessage = context.get()?.getString(R.string.something_went_wrong))
    }

    suspend fun like(type: String?, id: String?, action: String?): IResult<BaseResponse> {
        return getResponse(request = { api.like(type, id, action) }, defaultErrorMessage = context.get()?.getString(R.string.something_went_wrong))
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String?): IResult<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                if (result.body() is BaseResponse && (result.body() as BaseResponse).status != 1) {
                    IResult.error(Error(ErrorCode.UNKNOWN, (result.body() as BaseResponse).message ?: defaultErrorMessage?: Const.SOMETHING_WENT_WRONG))
                } else {
                    return IResult.success(result.body())
                }
            } else {
                val errorResponse = ErrorUtils.parseError(result, defaultErrorMessage ?: Const.SOMETHING_WENT_WRONG)
                IResult.error(errorResponse)
            }
        } catch (e: Throwable) {
            if (e is UnknownHostException || e is SocketTimeoutException) {
                IResult.error(Error(ErrorCode.CONNECTION, e.message ?: defaultErrorMessage ?: Const.SOMETHING_WENT_WRONG))
            } else {
                IResult.error(Error(ErrorCode.UNKNOWN, e.message ?: defaultErrorMessage ?: Const.SOMETHING_WENT_WRONG))
            }
        }
    }
}