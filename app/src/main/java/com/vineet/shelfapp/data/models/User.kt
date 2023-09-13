package com.vineet.shelfapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
                var id: Long?=null,
                @ColumnInfo(name = "name") var name: String?=null,
                @ColumnInfo(name = "country") var country: String?=null,
                @ColumnInfo(name = "password") var password: String?=null,
)