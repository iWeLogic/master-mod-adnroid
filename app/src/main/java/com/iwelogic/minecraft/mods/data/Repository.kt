package com.iwelogic.minecraft.mods.data

import android.util.ArrayMap
import com.iwelogic.minecraft.mods.models.*
import com.iwelogic.minecraft.mods.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(private val dataSource: DataSource, private val dataBaseSource: DataBaseSource) {

    fun checkExist(id: String) = dataBaseSource.checkExist(id)

    fun getFavouriteItems() = dataBaseSource.getFavouriteItems()

    suspend fun setFavourite(item: BaseItem): Flow<IResult<Any>> {
        return flow {
            emit(IResult.loading())
            emit(dataBaseSource.insertItemToFavourite(item))
            emit(IResult.finish())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun removeFromFavourite(item: BaseItem): Flow<IResult<Any>> {
        return flow {
            emit(IResult.loading())
            emit(dataBaseSource.removeFromFavourite(item))
            emit(IResult.finish())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getItems(category: String, pack: String?, sort: String?, page: Int, search: String? = null): Flow<IResult<ResponseData<BaseItem>>> {
        val queries = ArrayMap<String, Any>()
        queries["type"] = category
        queries["sort"] = sort
        queries["page"] = page
        pack?.let {
            queries["pack"] = it
        }
        search?.let {
            queries["search"] = it
        }
        return flow {
            emit(IResult.loading())
            emit(dataSource.getBaseItems(queries))
            emit(IResult.finish())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun increaseInstalls(type: String?, id: String?): Flow<IResult<BaseResponse>> {
        return flow {
            emit(IResult.loading())
            emit(dataSource.increaseInstalls(type, id))
            emit(IResult.finish())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun like(type: String?, id: String?, action: String?): Flow<IResult<BaseResponse>> {
        return flow {
            emit(IResult.loading())
            emit(dataSource.like(type, id, action))
            emit(IResult.finish())
        }.flowOn(Dispatchers.IO)
    }

    fun preloadAll(sort: String): Flow<IResult<PreloadData>> {
        return flow {
            emit(IResult.loading())
            val requestsAddons: MutableList<Deferred<IResult<ResponseData<*>>>> = ArrayList()
            //    val requestsBuildings: MutableList<Deferred<IResult<ResponseData<*>>>> = ArrayList()
            val requestsMaps: MutableList<Deferred<IResult<ResponseData<*>>>> = ArrayList()
            val requestsTextures: MutableList<Deferred<IResult<ResponseData<*>>>> = ArrayList()
            val requestsSeeds: MutableList<Deferred<IResult<ResponseData<*>>>> = ArrayList()
            val requestsSkins: MutableList<Deferred<IResult<ResponseData<*>>>> = ArrayList()
            withContext(Dispatchers.IO) {
                Category.ADDONS.subcategories.forEach {
                    requestsAddons.add(async {
                        val queries = ArrayMap<String, Any>()
                        queries["type"] = "addons"
                        queries["pack"] = it.id
                        queries["sort"] = sort
                        queries["page"] = 1
                        dataSource.getBaseItems(queries)
                    })
                }
                Category.MAPS.subcategories.forEach {
                    requestsMaps.add(async {
                        val queries = ArrayMap<String, Any>()
                        queries["type"] = "maps"
                        queries["pack"] = it.id
                        queries["sort"] = sort
                        queries["page"] = 1
                        dataSource.getBaseItems(queries)
                    })
                }
                Category.TEXTURES.subcategories.forEach {
                    requestsTextures.add(async {
                        val queries = ArrayMap<String, Any>()
                        queries["type"] = "textures"
                        queries["pack"] = it.id
                        queries["sort"] = sort
                        queries["page"] = 1
                        dataSource.getBaseItems(queries)
                    })
                }
                Category.SEEDS.subcategories.forEach {
                    requestsSeeds.add(async {
                        val queries = ArrayMap<String, Any>()
                        queries["type"] = "seeds"
                        queries["pack"] = it.id
                        queries["sort"] = sort
                        queries["page"] = 1
                        dataSource.getBaseItems(queries)
                    })
                }
                Category.SKINS.subcategories.forEach {
                    requestsSkins.add(async {
                        val queries = ArrayMap<String, Any>()
                        queries["type"] = "skins"
                        queries["pack"] = it.id
                        queries["sort"] = sort
                        queries["page"] = 1
                        dataSource.getBaseItems(queries)
                    })
                }
                /*  Category.BUILDINGS.subcategories.forEach {
                      requestsBuildings.add(async {
                          val queries = ArrayMap<String, Any>()
                          queries["type"] = "buildings"
                          queries["pack"] = it.id
                          queries["sort"] = sort
                          queries["page"] = 1
                          dataSource.getBaseItems(queries)
                      })
                  }*/
            }
            val results: MutableList<IResult<ResponseData<*>>> = ArrayList()
            results.addAll(requestsAddons.map { it.await() }.onEach { it.data?.data?.forEach { item -> (item as BaseItem).category = "addons" } })
            results.addAll(requestsMaps.map { it.await() }.onEach { it.data?.data?.forEach { item -> (item as BaseItem).category = "maps" } })
            results.addAll(requestsTextures.map { it.await() }.onEach { it.data?.data?.forEach { item -> (item as BaseItem).category = "textures" } })
            results.addAll(requestsSeeds.map { it.await() }.onEach { it.data?.data?.forEach { item -> (item as BaseItem).category = "seeds" } })
            // results.addAll(requestsBuildings.map { it.await() }.onEach { it.data?.data?.forEach { item -> (item as BaseItem).category = "buildings" } })
            results.addAll(requestsSkins.map { it.await() }.onEach { it.data?.data?.forEach { item -> (item as BaseItem).category = "skins" } })

            val error = results.firstOrNull { it.error != null }
            error?.let {
                emit(IResult<PreloadData>(IResult.Status.ERROR, null, it.error))
            } ?: run {
                val preloadData = PreloadData()
                results.forEach { res ->
                    val pack: String = (res.data?.data?.firstOrNull() as BasePack?)?.pack ?: "0"
                    when ((res.data?.data?.firstOrNull() as BasePack?)?.category ?: "addons") {
                        "addons" -> preloadData.items.add(BaseListItemData(pack = pack, items = res.data?.data?.map { it as BaseItem }))
                        "maps" -> preloadData.items.add(BaseListItemData(pack = pack, items = res.data?.data?.map { it as BaseItem }))
                        "textures" -> preloadData.items.add(BaseListItemData(pack = pack, items = res.data?.data?.map { it as BaseItem }))
                        "seeds" -> preloadData.items.add(BaseListItemData(pack = pack, items = res.data?.data?.map { it as BaseItem }))
                        "buildings" -> preloadData.items.add(BaseListItemData(pack = pack, items = res.data?.data?.map { it as BaseItem }))
                        "skins" -> preloadData.items.add(BaseListItemData(pack = pack, items = res.data?.data?.map { it as BaseItem }))
                    }
                }
                emit(IResult(IResult.Status.SUCCESS, preloadData, null))
            }
            emit(IResult.finish())
        }.flowOn(Dispatchers.IO)
    }
}