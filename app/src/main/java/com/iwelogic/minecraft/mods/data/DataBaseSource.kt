package com.iwelogic.minecraft.mods.data

import android.content.Context
import androidx.room.Room
import com.iwelogic.minecraft.mods.models.BaseItem
import com.iwelogic.minecraft.mods.models.ResponseData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataBaseSource @Inject constructor(private val api: Api, @ApplicationContext applicationContext: Context) {

    var dataBase = Room.databaseBuilder(applicationContext, DataBase::class.java, "statistics").allowMainThreadQueries().build()

    suspend fun insertItemToFavourite(item: BaseItem): Result<ResponseData<Any>> {
        dataBase.itemDao().insert(item)
        return Result.Success(null)
    }

    suspend fun removeFromFavourite(item: BaseItem): Result<ResponseData<Any>> {
        dataBase.itemDao().removeFromFavourite(item.primaryId)
        return Result.Success(null)
    }

    fun checkExist(id: String) = dataBase.itemDao().checkExist(id)

    fun getFavouriteItems() = dataBase.itemDao().getFavouriteItems()
}