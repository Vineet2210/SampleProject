package com.vineet.shelfapp.data.repo

import com.vineet.shelfapp.data.dao.AuthDao
import com.vineet.shelfapp.data.models.User
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AuthRepo @Inject constructor(

){
    @Inject
    lateinit var authDao: AuthDao
    suspend fun insertUser(userData: User) {
        authDao.insert(userData)
    }

    fun authenticateUser(name: String, password: String)=authDao.authenticate(name, password)

}