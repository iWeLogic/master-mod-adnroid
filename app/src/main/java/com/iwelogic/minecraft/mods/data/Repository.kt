package com.iwelogic.minecraft.mods.data

import com.iwelogic.minecraft.mods.models.Mod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(private val dataSource: DataSource, private val dataBaseSource: DataBaseSource) {

/*    fun checkExist(id: String) = dataBaseSource.checkExist(id)

    fun getFavouriteItems() = dataBaseSource.getFavouriteItems()*/

    /*  suspend fun setFavourite(item: BaseItem): Flow<Result<Any>> {
          return flow {
              emit(Result.loading())
              emit(dataBaseSource.insertItemToFavourite(item))
              emit(Result.finish())
          }.flowOn(Dispatchers.IO)
      }

      suspend fun removeFromFavourite(item: BaseItem): Flow<Result<Any>> {
          return flow {
              emit(Result.loading())
              emit(dataBaseSource.removeFromFavourite(item))
              emit(Result.finish())
          }.flowOn(Dispatchers.IO)
      }
  */
    suspend fun getMods(category: String, queries: MultiMap<String, Any>): Flow<Result<List<Mod>>> {
        return flow {
            emit(Result.Loading)
            emit(dataSource.getMods(category, queries))
            emit(Result.Finish)
        }.flowOn(Dispatchers.IO)
    }

    /* suspend fun increaseInstalls(type: String?, id: String?): Flow<Result<BaseResponse>> {
         return flow {
             emit(Result.loading())
             emit(dataSource.increaseInstalls(type, id))
             emit(Result.finish())
         }.flowOn(Dispatchers.IO)
     }

     suspend fun like(type: String?, id: String?, action: String?): Flow<Result<BaseResponse>> {
         return flow {
             emit(Result.loading())
             emit(dataSource.like(type, id, action))
             emit(Result.finish())
         }.flowOn(Dispatchers.IO)
     }*/
}