package com.vineet.shelfapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
val status: String,
val statusCode: Long,
val version: String,
val access: String,
val data: Map<String, Datum>
):Parcelable {
    @Parcelize
    data class Datum(
        val country: String,
        val region: String,
    ):Parcelable
}

