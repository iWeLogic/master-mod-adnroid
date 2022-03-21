package com.iwelogic.minecraft.mods.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

private const val SETTINGS = "settings"

fun Context.writeString(key: String, value: String) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putString(key, value).apply()
}

fun Context.readString(key: String, default: String): String {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getString(key, default) ?: default
}

fun Context.readString(key: String): String? {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getString(key, null)
}

fun Context.writeBoolean(key: String, value: Boolean) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()
}

@SuppressLint("ApplySharedPref")
fun Context.writeBooleanImmediately(key: String, value: Boolean) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit()
}

fun Context.readBoolean(key: String, def: Boolean): Boolean {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getBoolean(key, def)
}

fun Context.readBoolean(key: String): Boolean {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getBoolean(key, false)
}

fun Context.writeLong(key: String, value: Long) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putLong(key, value).apply()
}

fun Context.readLong(key: String, default: Long): Long {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getLong(key, default)
}

fun Context.readLong(key: String): Long {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getLong(key, 0)
}

fun Context.writeInteger(key: String, value: Int) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
}

fun Context.readInteger(key: String): Int {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getInt(key, 0)
}

fun Context.readInteger(key: String, default: Int): Int {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getInt(key, default)
}

fun Context.writeFloat(key: String, value: Float) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putFloat(key, value).apply()
}

fun Context.readFloat(key: String): Float {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getFloat(key, 0f)
}

fun Context.readFloat(key: String, default: Float): Float {
    return getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getFloat(key, default)
}

fun Context.writeObject(key: String, value: Any) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putString(key, Gson().toJson(value)).apply()
}

@SuppressLint("ApplySharedPref")
fun Context.writeObjectImmediately(key: String, value: Any) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putString(key, Gson().toJson(value)).commit()
}

fun <T> Context.readObject(key: String, type: Class<T>): T? {
    return Gson().fromJson(getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getString(key, ""), type)
}

fun <T> Context.writeList(key: String, items: List<T>) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().putString(key, Gson().toJson(items, object : TypeToken<List<T>?>() {}.type)).apply()
}

fun <T> Context.readList(key: String, type: Class<T>): List<T> {
    return Gson().fromJson(getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).getString(key, ""), ListOfSomething(type))
}

fun Context.remove(key: String?) {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().remove(key).apply()
}

fun Context.clearData() {
    getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit().clear().apply()
}

internal class ListOfSomething<X>(wrapped: Class<X>) : ParameterizedType {

    private val wrapped: Class<*> = wrapped

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(wrapped)
    }

    override fun getRawType(): Type {
        return MutableList::class.java
    }

    override fun getOwnerType(): Type? {
        return null
    }
}
