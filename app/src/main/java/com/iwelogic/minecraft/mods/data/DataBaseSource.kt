package com.iwelogic.minecraft.mods.data

import android.content.Context
import androidx.room.Room
import com.iwelogic.minecraft.mods.models.BaseItem
import com.iwelogic.minecraft.mods.models.ResponseData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataBaseSource @Inject constructor(private val api: Api, @ApplicationContext applicationContext: Context) {

    var dataBase = Room.databaseBuilder(applicationContext, DataBase::class.java, "statistics").allowMainThreadQueries().build()

    suspend fun insertItemToFavourite(item: BaseItem): IResult<ResponseData<Any>> {
        dataBase.itemDao().insert(item)
        return IResult(IResult.Status.SUCCESS, null, null)
    }

    suspend fun removeFromFavourite(item: BaseItem): IResult<ResponseData<Any>> {
        dataBase.itemDao().removeFromFavourite(item.primaryId)
        return IResult(IResult.Status.SUCCESS, null, null)
    }

    fun checkExist(id: String) = dataBase.itemDao().checkExist(id)

    fun getFavouriteItems() = dataBase.itemDao().getFavouriteItems()
}