package com.vineet.shelfapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.data.models.User
import kotlinx.coroutines.flow.Flow


// operation performed for user onboarding
@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(userList: User)

     @Query("SELECT * FROM `users` WHERE name=:name AND password=:password")
     fun authenticate(name:String,password:String): Flow<User?>

}