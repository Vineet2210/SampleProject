package com.vineet.shelfapp.data.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
class BookListResponse : ArrayList<BookListResponse.BookListResponseItem>(), Parcelable {
    @Parcelize
    data class BookListResponseItem(
        val alias: String?=null,
        val hits: Int?=null,
        val id: String?=null,
        val image: String?=null,
        val lastChapterDate: Long?=null,
        val title: String?=null,
        var isFavorite :Boolean?=null
    ) : Parcelable
}