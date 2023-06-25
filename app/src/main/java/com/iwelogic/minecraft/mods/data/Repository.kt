package com.iwelogic.minecraft.mods.data

import com.iwelogic.minecraft.mods.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataBaseSource: DataBaseSource,
    private val dataProvider: DataProvider
) {

    fun checkExist(id: String) = dataBaseSource.checkExist(id)

    fun getFavouriteItems() = dataBaseSource.getFavouriteItems()


    suspend fun getMods(type: Type,  sort: Sort, filter: List<FilterValue>): Flow<Result<List<Mod>>> {
        return flow {
            emit(Result.Loading)
            delay(200)
            emit(dataProvider.getData(type, sort, filter.filter { it.value }.map { it.filter.id.toInt() }))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMods(type: Type,  query: String, filter: List<FilterValue>): Flow<Result<List<Mod>>> {
        return flow {
            emit(Result.Loading)
            delay(200)
            emit(dataProvider.getData(type, query, filter.filter { it.value }.map { it.filter.id.toInt() }))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setFavourite(item: Mod): Flow<Result<Any>> {
        return flow {
            emit(Result.Loading)
            emit(dataBaseSource.insertItemToFavourite(item))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun removeFromFavourite(item: Mod): Flow<Result<Any>> {
        return flow {
            emit(Result.Loading)
            emit(dataBaseSource.removeFromFavourite(item))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }
}