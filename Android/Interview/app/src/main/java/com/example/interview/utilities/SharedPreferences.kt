package com.example.interview.utilities

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferences<T>(private val context: Context) {

    companion object {
        private const val PREF_NAME = "auuth_prefs"
        private const val PREF_KEY = "auuth_key"

        fun <T> saveArrayListToSharedPreferences(context: Context, arrayList: ArrayList<T>) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(arrayList)
            editor.putString(PREF_KEY, json)
            editor.apply()
        }

        fun <T> getArrayListFromSharedPreferences(context: Context): ArrayList<T> {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString(PREF_KEY, null)
            val type: Type = object : TypeToken<ArrayList<T>>() {}.type
            return gson.fromJson(json, type) ?: ArrayList()
        }
    }

    fun saveArrayList(key: String, arrayList: ArrayList<T>) {
        saveArrayListToSharedPreferences(context, arrayList)
    }

    fun getArrayList(key: String): ArrayList<T> {
        return getArrayListFromSharedPreferences(context)
    }
}
