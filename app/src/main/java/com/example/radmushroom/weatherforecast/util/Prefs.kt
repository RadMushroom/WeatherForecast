package com.example.radmushroom.weatherforecast.util

import android.app.Activity
import android.content.SharedPreferences


class Prefs(activity: Activity) {

    var preferences: SharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE)

    fun setLocation(location: String){
        preferences.edit().putString("location", location).apply()
    }

    fun getLocation(): String{
        return preferences.getString("location", "kyiv")
    }
}