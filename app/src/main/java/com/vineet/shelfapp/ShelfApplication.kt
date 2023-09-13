package com.vineet.shelfapp

import android.app.Application
import com.vineet.shelfapp.utils.ShelfPreference
import dagger.hilt.android.HiltAndroidApp

//Application Class
@HiltAndroidApp
class ShelfApplication:Application() {
    //initialising shared preferences
    override fun onCreate() {
        super.onCreate()
        ShelfPreference.init(this)
    }
}