package com.vineet.shelfapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vineet.shelfapp.data.repo.BookRepo
import com.vineet.shelfapp.data.models.BookListResponse
import com.vineet.shelfapp.data.models.Favorites
import com.vineet.shelfapp.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel@Inject constructor(
    private val repository: BookRepo,
): ViewModel() {
    private val _bookListResponseMLD = MutableLiveData<NetworkResponse<BookListResponse>>()
    val favList = MutableLiveData<MutableList<Favorites>>()
    fun observeBookListResponse(): MutableLiveData<NetworkResponse<BookListResponse>> {
        return _bookListResponseMLD
    }


    fun getBookList() = viewModelScope.launch {
    repository.getBookList().collect {
        if (it.data?.isNotEmpty() == true)
            _bookListResponseMLD.value = NetworkResponse.success(it.data)
        }
    }

    fun insertFav(favData:Favorites) {
        viewModelScope.launch {
            repository.insertFav(favData)
        }
    }
    fun deleteFav(favData:Favorites) {
        viewModelScope.launch {
            repository.deleteFav(favData)
        }
    }

    fun getFavBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchFav().collect {
                if (it.isNotEmpty()) {
                    favList.postValue(it as MutableList<Favorites>)
                }
            }
        }
    }
}