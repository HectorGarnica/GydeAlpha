package com.example.gydealpha.util

import android.content.Context

class MyPreference (context: Context){


    val PREFERENCE_NAME = "sharedPreferenceExample"
    val PREFERENCE_USER_NAME = "username"
    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getUserName(): String? {
        return preference.getString(PREFERENCE_USER_NAME,"YOUR NAME")
    }

    fun setUserName(name:String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_NAME,name)
        editor.apply()
    }

}