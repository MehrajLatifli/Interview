package com.example.interview.utilities

import android.content.Context
import androidx.fragment.app.Fragment

object Constants {

    private lateinit var context: Context

    val Base_URL = "https://${context.getSharedPreferences("baseURL_local", Context.MODE_PRIVATE)}/"

}
