package com.example.interview.utilities

import java.net.HttpURLConnection
import java.net.URL

object Constants {
    private const val PRODUCTION_URL = "https://192.168.22.189:50005/api/v1/"
    private const val BACKUP_URL = "https://192.168.22.6:50005/api/v1/"
    var Base_URL = BACKUP_URL



}
