package com.vineet.shelfapp.data.repo

import com.vineet.shelfapp.data.dao.FavoriteDao
import com.vineet.shelfapp.data.models.BookListResponse
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.networkmodule.ApiInterface
import com.vineet.shelfapp.utils.NetworkResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ViewModelScoped
class BookRepo  @Inject constructor(private val apiInterface: ApiInterface) {

    @Inject
    lateinit var favoriteDao: FavoriteDao

    suspend fun getBookList(): Flow<NetworkResponse<BookListResponse>> {
        return flow {
            val response = apiInterface.getBookList()
            emit( NetworkResponse.success(response))
        }.flowOn(Dispatchers.IO)
    }
    suspend fun insertFav(favData:Favorites) {
        favoriteDao.insertFavourite(favData)
    }

    suspend fun deleteFav(favData:Favorites) {
        favoriteDao.deleteFavorite(favData)
    }
     fun fetchFav()=favoriteDao.fetchFavorites()

}