package com.iwelogic.minecraft.mods.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iwelogic.minecraft.mods.models.BaseItem

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: BaseItem)

    @Query("DELETE FROM items WHERE primary_id = :primaryId")
    suspend fun removeFromFavourite(primaryId: String?)

    @Query("SELECT EXISTS(SELECT * from items WHERE primary_id = :primaryId)")
    fun checkExist(primaryId: String): LiveData<Boolean>

    @Query("SELECT * from items")
    fun getFavouriteItems(): LiveData<List<BaseItem>>

/*    @Query("SELECT * from profile WHERE id = 0")
    fun getProfile(): LiveData<BaseItem>

    @Query("SELECT * from profile WHERE id = 0")
    fun getProfileSync(): Profile?

    @Query("SELECT goal_steps from profile WHERE id = 0")
    fun getStepsGoal(): LiveData<Int>

    @Query("SELECT goal_drinks from profile WHERE id = 0")
    fun getDrinkGoal(): LiveData<Int>

    @Query("SELECT lang from profile WHERE id = 0")
    fun getLang(): LiveData<Int>

    @Query("SELECT unit from profile WHERE id = 0")
    fun getUnit(): LiveData<Int>

    @Query("SELECT reminder_time from profile WHERE id = 0")
    fun getReminderTime(): LiveData<String>*/

//    @Query("SELECT gender from profile WHERE id = 0")
//    fun getGender(): LiveData<Int>
//
//    @Query("SELECT age from profile WHERE id = 0")
//    fun getAge(): LiveData<Int>
//
//    @Query("SELECT height from profile WHERE id = 0")
//    fun getHeight(): LiveData<Int>
//
//    @Query("SELECT weight from profile WHERE id = 0")
//    fun getWeight(): LiveData<Int>

}