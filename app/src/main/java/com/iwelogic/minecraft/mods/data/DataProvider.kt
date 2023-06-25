package com.iwelogic.minecraft.mods.data

import android.content.Context
import com.google.gson.Gson
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.utils.isTrue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DataProvider @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val providerScope = CoroutineScope(Job() + Dispatchers.Main)
    private var addons: List<Mod> = listOf()
    private var maps: List<Mod> = listOf()
    private var textures: List<Mod> = listOf()
    private var seeds: List<Mod> = listOf()
    private var skins: List<Mod> = listOf()

    init {
        providerScope.launch {
            addons = loadData(Type.ADDONS)
        }
        providerScope.launch {
            maps = loadData(Type.MAPS)
        }
        providerScope.launch {
            textures = loadData(Type.TEXTURES)
        }
        providerScope.launch {
            seeds = loadData(Type.SEEDS)
        }
        providerScope.launch {
            skins = loadData(Type.SKINS)
        }
    }

    private suspend fun loadData(type: Type): List<Mod> {
        return suspendCoroutine { continuation ->
            val text: String =
                BufferedReader(InputStreamReader(applicationContext.assets.open("${type.id}.json"))).readText()
            val temp = Gson().fromJson(text, Array<Mod>::class.java).toList()
            temp.onEach {
                it.type = type
            }
            continuation.resume(temp.shuffled())
        }
    }

    suspend fun getData(type: Type, sort: Sort, filter: List<Int>): Result<List<Mod>> {

        var temp = when (type) {
            Type.ADDONS -> addons
            Type.MAPS -> maps
            Type.SEEDS -> seeds
            Type.TEXTURES -> textures
            Type.SKINS -> skins
        }

        temp = temp.ifEmpty {
            loadData(type)
        }.filter {
            filter.isEmpty() || filter.contains(it.pack)
        }

        temp = when (sort) {
            Sort.DOWNLOADS -> temp.sortedBy { it.installs }.reversed()
            Sort.LIKES -> temp.sortedBy { it.likes }.reversed()
            Sort.FILE_SIZE_DESC -> temp.sortedBy { it.fileSize }.reversed()
            Sort.FILE_SIZE_ASC -> temp.sortedBy { it.fileSize }
            else -> temp.shuffled()
        }
        return Result.Success(temp)
    }

    suspend fun getData(type: Type, query: String, filter: List<Int>): Result<List<Mod>> {

        if (query.isEmpty())
            return Result.Success(listOf())
        
        var temp = when (type) {
            Type.ADDONS -> addons
            Type.MAPS -> maps
            Type.SEEDS -> seeds
            Type.TEXTURES -> textures
            Type.SKINS -> skins
        }

        temp = temp.ifEmpty {
            loadData(type)
        }.filter {
            filter.isEmpty() || filter.contains(it.pack)
        }
        temp = temp.filter {
            it.title?.contains(query, true).isTrue()
        }

        return Result.Success(temp)
    }
}