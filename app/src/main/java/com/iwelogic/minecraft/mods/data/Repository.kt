package com.iwelogic.minecraft.mods.data

import com.iwelogic.minecraft.mods.models.Mod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(private val dataSource: DataSource, private val dataBaseSource: DataBaseSource) {

    fun checkExist(id: String) = dataBaseSource.checkExist(id)

    fun getFavouriteItems() = dataBaseSource.getFavouriteItems()

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

    suspend fun getMods(category: String, queries: MultiMap<String, Any>): Flow<Result<List<Mod>>> {
        return flow {
            emit(Result.Loading)
            delay(3300)
            emit(dataSource.getMods(category, queries))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateMod(category: String, mod: Mod): Flow<Result<Any>> {
        return flow {
            emit(Result.Loading)
            emit(dataSource.updateMod(category, mod))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }
}