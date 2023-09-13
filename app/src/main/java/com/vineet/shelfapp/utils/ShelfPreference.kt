package com.vineet.shelfapp.utils

import android.content.Context
import android.content.SharedPreferences

object ShelfPreference {
    private const val tokenPreference = "Session"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(tokenPreference, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    // method for saving String values in preferences
    fun savePreference(key:String? ,token: String?) {
        editor.apply {
            putString(key, token)
            apply()
        }
    }

    // method for retrieving String values in preferences
    fun getPreference(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    // method for saving Boolean values in preferences
    fun saveBoolPreference(key:String? ,token: Boolean?) {
        editor.apply {
            if (token != null) {
                putBoolean(key, token)
            }
            apply()
        }
    }

    // method for retrieving Boolean values in preferences
    fun getBoolPreference(key:String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}
