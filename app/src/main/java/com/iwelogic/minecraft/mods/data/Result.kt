package com.iwelogic.minecraft.mods.data

import com.google.gson.annotations.SerializedName

sealed class Result<out T> {

    data class Success<out R>(val data: R?) : Result<R>()

    data class Error(val code: Code, val message: String?) : Result<Nothing>() {

        enum class Code {

            @SerializedName("3033")
            ALREADY_EXISTS,

            @SerializedName("3087")
            NOT_CONFIRMED,

            @SerializedName("3003")
            WRONG_EMAIL_OR_PASSWORD,

            UNKNOWN,

            NO_CONNECTION,

            AUTH;
        }
    }

    object Loading : Result<Nothing>()

    object Finish : Result<Nothing>()
}

