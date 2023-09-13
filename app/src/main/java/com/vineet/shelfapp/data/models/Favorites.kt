package com.vineet.shelfapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "favorites")
data class Favorites(
                @PrimaryKey @ColumnInfo (name= "bookId")var id: String,
                @ColumnInfo(name = "isFavorite") var isFavorite: Boolean?=null,
)