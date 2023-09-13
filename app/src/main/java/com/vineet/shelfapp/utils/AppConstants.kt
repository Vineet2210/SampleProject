package com.vineet.shelfapp.utils

import com.vineet.shelfapp.data.models.Country

object AppConstants {
    // Api EndPoints Constants
    object ApiEndPoints {
        const val BOOK_LIST = "/b/ZEDF"
    }

    // Bundle Constants used to transfer data
    object BundleConstants{
        const val BOOK_DETAILS="bookDetails"
    }

    // sorting pre-defined constants
    object SortingTypes{
        const val TITLE=1
        const val HITS=2
    }

    //Preference Constants
    object PreferenceConstants{
        const val GET_TOKEN="getToken"
        const val USER_SORT_PREFERENCE="userSortPreference"
    }
}