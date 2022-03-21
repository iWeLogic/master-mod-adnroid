package com.iwelogic.minecraft.mods.utils

import java.util.regex.Pattern

private val EMAIL: Pattern = Pattern.compile("[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+")

fun String?.isEmail(): Boolean {
    return this?.let {
        EMAIL.matcher(trim { it <= ' ' }).matches()
    } ?: false
}

fun String?.isPhone(pattern: Pattern): Boolean {
    return this?.let {
        pattern.matcher(trim { it <= ' ' }).matches()
    } ?: false
}