package com.iwelogic.minecraft.mods.data

sealed class Result<out T> {

    data class Success<out R>(val data: R?) : Result<R>()

    data class Error(val code: Code, val message: String?) : Result<Nothing>() {

        enum class Code {

            UNKNOWN,

            NO_CONNECTION,
        }
    }

    object Loading : Result<Nothing>()

    object Finish : Result<Nothing>()
}

