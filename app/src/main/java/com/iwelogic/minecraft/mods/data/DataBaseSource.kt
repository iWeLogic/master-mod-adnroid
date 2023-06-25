package com.iwelogic.minecraft.mods.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iwelogic.minecraft.mods.models.Mod
import com.iwelogic.minecraft.mods.models.Type
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class DataBaseSource @Inject constructor(@ApplicationContext applicationContext: Context) {

    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        @SuppressLint("Range")
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE mods ADD COLUMN type TEXT")
            val cursor = database.query("SELECT primary_id, category FROM mods")
            try {
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getString(cursor.getColumnIndex("primary_id"))
                        val category = cursor.getString(cursor.getColumnIndex("category"))
                        val type = Type.getValueById(category)
                        val values = ContentValues()
                        values.put("type", type.toString().uppercase())
                        database.update("mods", CONFLICT_REPLACE, values, "primary_id=?", arrayOf<String>(id))
                    } while (cursor.moveToNext())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (!cursor.isClosed) {
                    cursor.close()
                }
            }
        }
    }

    private var dataBase = Room.databaseBuilder(applicationContext, DataBase::class.java, "statistics")
        .addMigrations(MIGRATION_1_2)
        .allowMainThreadQueries().build()

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