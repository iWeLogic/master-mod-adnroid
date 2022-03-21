package com.iwelogic.minecraft.mods.data

import retrofit2.Response

object ErrorUtils {

    fun parseError(response: Response<*>, defaultMessage: String): Error {
        return try {
            if (response.code() >= 404) {
                Error(ErrorCode.CONNECTION, defaultMessage)
            } else {
                Error(ErrorCode.UNKNOWN, defaultMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Error(ErrorCode.UNKNOWN, e.message ?: defaultMessage)
        }
    }
}