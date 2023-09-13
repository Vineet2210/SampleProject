package com.vineet.shelfapp.networkmodule

import com.vineet.shelfapp.data.models.BookListResponse
import com.vineet.shelfapp.utils.AppConstants
import retrofit2.http.GET


interface ApiInterface {

    @GET(AppConstants.ApiEndPoints.BOOK_LIST)
    suspend fun getBookList():BookListResponse
}