package com.iwelogic.minecraft.mods.utils

import com.google.gson.Gson
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.coroutineContext

fun List<*>?.deepEquals(other: List<*>?): Boolean {
    if (this == null && other != null || this != null && other == null) return false
    if (this == null && other == null) return true
    return this!!.size == other!!.size && this.mapIndexed { index, element -> element == other[index] }.all { it }
}

inline fun <reified T> Gson.deepCopy(value: T): T {
    return fromJson(toJson(value), T::class.java)
}

inline fun <reified T> List<T>?.deepCopy(): List<T>? {
    val temp: MutableList<T> = ArrayList()
    val gson = Gson()
    this?.forEach {
        temp.add(gson.fromJson(gson.toJson(it), T::class.java))
    }
    return if(this == null) null else temp.toList()
}

fun Boolean?.isTrue(action: () -> Unit) {
    if (this == true) {
        action.invoke()
    }
}

fun Boolean?.isTrue(): Boolean {
    return this == true
}

suspend inline fun <T> Flow<T>.safeCollect(crossinline action: suspend (T) -> Unit) {
    collect {
        coroutineContext.ensureActive()
        action(it)
    }
}

inline fun catchAll(action: () -> Unit) {
    try {
        action()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}