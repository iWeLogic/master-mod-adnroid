package com.iwelogic.minecraft.mods.data


data class IResult<out T>(val status: Status, val data: T?, val error: Error?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        FINISH
    }

    companion object {
        fun <T> success(data: T?): IResult<T> {
            return IResult(Status.SUCCESS, data, null)
        }

        fun <T> error(error: Error): IResult<T> {
            return IResult(Status.ERROR, null, error)
        }

        fun <T> loading(data: T? = null): IResult<T> {
            return IResult(Status.LOADING, data, null)
        }

        fun <T> finish(data: T? = null): IResult<T> {
            return IResult(Status.FINISH, data, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error)"
    }
}