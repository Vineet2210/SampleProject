package com.vineet.shelfapp.networkmodule

import android.content.Context
import com.vineet.shelfapp.data.dao.AuthDao
import com.vineet.shelfapp.data.DataBaseInstance
import com.vineet.shelfapp.data.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//Module class to provide dependency for injections
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun getUserDao(databaseInstance: DataBaseInstance): AuthDao {
        return databaseInstance.authDao()
    }

    @Singleton
    @Provides
    fun getFavDao(databaseInstance: DataBaseInstance): FavoriteDao {
        return databaseInstance.favDao()
    }

    @Singleton
    @Provides
    fun getDbInstance(@ApplicationContext context:Context): DataBaseInstance
    {
        return DataBaseInstance.getInstance(context)
    }

}