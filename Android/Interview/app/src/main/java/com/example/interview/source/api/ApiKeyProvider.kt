package com.example.interview.source.api

import android.content.Context
import javax.inject.Inject

class ApiKeyProvider @Inject constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getApiKey(): String? {
        return sharedPreferences.getString("api_key", null)
    }
}