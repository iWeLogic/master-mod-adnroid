package com.iwelogic.minecraft.mods.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iwelogic.minecraft.mods.models.Mod

@Dao
interface ModDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mod: Mod)

    @Query("DELETE FROM mods WHERE primary_id = :primaryId")
    suspend fun removeFromFavourite(primaryId: String?)

    @Query("SELECT EXISTS(SELECT * from mods WHERE primary_id = :primaryId)")
    fun checkExist(primaryId: String): LiveData<Boolean>

    @Query("SELECT * from mods")
    fun getFavouriteItems(): LiveData<List<Mod>>

}