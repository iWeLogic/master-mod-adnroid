package com.iwelogic.minecraft.mods.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iwelogic.minecraft.mods.models.Mod

@Database(entities = [Mod::class], version = 1, exportSchema = false)
abstract class DataBase : RoomDatabase() {

    abstract fun itemDao(): ModDao
}