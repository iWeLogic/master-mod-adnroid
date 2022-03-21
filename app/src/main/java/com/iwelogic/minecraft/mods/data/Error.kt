package com.iwelogic.minecraft.mods.data

data class Error(val code: ErrorCode, val message: String)

enum class ErrorCode(val code: String) {
    UNKNOWN("unknown"),
    CONNECTION("connection");

    companion object {
        fun from(code: String): ErrorCode = values().firstOrNull { it.code == code } ?: UNKNOWN
    }
}