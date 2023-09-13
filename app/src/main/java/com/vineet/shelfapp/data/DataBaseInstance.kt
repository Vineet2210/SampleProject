package com.vineet.shelfapp.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import com.vineet.shelfapp.data.dao.AuthDao
import com.vineet.shelfapp.data.dao.FavoriteDao
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.data.models.User

@Database(entities = [User::class,Favorites::class], version = 2 , exportSchema = false)
abstract class DataBaseInstance : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun favDao(): FavoriteDao

    companion object {
        private var INSTANCE: DataBaseInstance ?= null

        fun getInstance(context: Context): DataBaseInstance {
            if (INSTANCE == null) {
                synchronized(DataBaseInstance::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DataBaseInstance::class.java, "shelf.db"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
    fun destroyInstance() {
        INSTANCE = null
    }
}