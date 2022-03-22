package com.iwelogic.minecraft.mods.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {

/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: BaseItem)

    @Query("DELETE FROM items WHERE primary_id = :primaryId")
    suspend fun removeFromFavourite(primaryId: String?)

    @Query("SELECT EXISTS(SELECT * from items WHERE primary_id = :primaryId)")
    fun checkExist(primaryId: String): LiveData<Boolean>

    @Query("SELECT * from items")
    fun getFavouriteItems(): LiveData<List<BaseItem>>*/

}