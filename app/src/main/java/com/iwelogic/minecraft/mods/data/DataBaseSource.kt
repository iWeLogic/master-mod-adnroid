package com.iwelogic.minecraft.mods.data

import android.content.Context
import androidx.room.Room
import com.iwelogic.minecraft.mods.models.Mod
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataBaseSource @Inject constructor(@ApplicationContext applicationContext: Context) {

    var dataBase = Room.databaseBuilder(applicationContext, DataBase::class.java, "statistics").allowMainThreadQueries().build()

    suspend fun insertItemToFavourite(item: Mod): Result<Any> {
        dataBase.itemDao().insert(item)
        return Result.Success(null)
    }

    suspend fun removeFromFavourite(item: Mod): Result<Any> {
        dataBase.itemDao().removeFromFavourite(item.primaryId)
        return Result.Success(null)
    }

    fun checkExist(id: String) = dataBase.itemDao().checkExist(id)

    fun getFavouriteItems() = dataBase.itemDao().getFavouriteItems()
}