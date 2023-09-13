package com.vineet.shelfapp.data.dao


import androidx.room.*
import androidx.room.Dao
import com.vineet.shelfapp.data.models.Favorites
import kotlinx.coroutines.flow.Flow

// operation performed for favorite feature
@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favData:Favorites)

    @Delete
    suspend fun deleteFavorite(favData: Favorites)

    @Query("SELECT * FROM `favorites`")
     fun fetchFavorites(): Flow<List<Favorites>>
}