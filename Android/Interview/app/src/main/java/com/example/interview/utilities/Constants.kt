package com.example.interview.utilities

import java.net.HttpURLConnection
import java.net.URL

object Constants {
    private const val PRODUCTION_URL = "https://192.168.22.189:4444/api/v1/"
    private const val BACKUP_URL = "https://192.168.22.6:4444/api/v1/"
    var Base_URL: String
    var API_KEY = ""
    var RefreshToken = ""

    init {
        if (!isUrlReachable(PRODUCTION_URL)) {
            Base_URL = BACKUP_URL
        }
        else{
            Base_URL = PRODUCTION_URL
        }
    }

    private fun isUrlReachable(url: String): Boolean {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 3000
            connection.connect()
            val responseCode = connection.responseCode
            connection.disconnect()
            responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            false
        }
    }
}
