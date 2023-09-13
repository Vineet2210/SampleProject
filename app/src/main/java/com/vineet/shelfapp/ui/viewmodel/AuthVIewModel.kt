package com.vineet.shelfapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vineet.shelfapp.data.models.User
import com.vineet.shelfapp.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVIewModel @Inject constructor(
    private val repository: AuthRepo,
): ViewModel() {

     val user: MutableLiveData<Boolean> = MutableLiveData()
     private val authenticatedUserData: MutableLiveData<User?> = MutableLiveData()

     val countryMld=MutableLiveData<String>()

    fun insertUser(user: User) {
       viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun authenticateUser(name:String,password:String) {
        viewModelScope.launch {
           val dbResponse= repository.authenticateUser(name,password)
            dbResponse.collect{
                if (it==null){
                    user.postValue(false)
                }
                else{
                    user.postValue(true)
                    authenticatedUserData.postValue(it)
                }
            }
        }
    }
}