package com.example.interview.source.api

import android.content.Context
import android.os.HandlerThread
import android.util.Log
import com.example.interview.models.responses.post.token.RefreshTokenRequest
import com.example.interview.source.api.IApiManager
import kotlinx.coroutines.*
import java.util.concurrent.Semaphore
import javax.inject.Inject

class RefreshTokenDetector @Inject constructor(
    private val apiManager: IApiManager,
    private val context: Context
) {

    private var job: Job? = null
    private var handlerThread: HandlerThread? = null


    private val semaphore = Semaphore(1)

    init {
        handlerThread = HandlerThread("RefreshTokenHandlerThread").apply { start() }
        startTokenRefreshing()
    }

    fun startTokenRefreshing() {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(2 * 1000)

            while (isActive) {
                if (getUserAuth() == true) {
                    delay(25 * 60 * 1000)
                    try {
                        semaphore.acquire()
                        refreshToken()
                    } finally {
                        semaphore.release()
                    }
                }
            }
        }
    }

    private suspend fun refreshToken() {
        try {

            val accessToken = getApiKey()
            val refreshToken = getRefreshToken()

            if (getUserAuth() == true) {
                if (accessToken != null && refreshToken != null) {

                    val response = apiManager.refreshToken(RefreshTokenRequest(accessToken, refreshToken))
                    if (response.isSuccessful) {
                        val tokenResponse = response.body()
                        if (tokenResponse != null) {

                            saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                            Log.e("RefreshTokenDetector", "Tokens refreshed successfully")
                        } else {
                            Log.e("RefreshTokenDetector", "Token response body is null")
                        }
                    } else {
                        Log.e("RefreshTokenDetector", "Failed to refresh tokens ${response.code()} - ${response.message()}")
                    }
                } else {
                    Log.e("RefreshTokenDetector", "Access token or refresh token is null")
                }
            }
        } catch (e: Exception) {
            Log.e("RefreshTokenDetector", "Exception refreshing tokens: ${e.message}")
        }
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {

        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("api_key", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    fun stop() {
        job?.cancel()
        handlerThread?.quitSafely()
    }

    private fun getUserAuth(): Boolean {
        val sp = context.getSharedPreferences("authresult_local", Context.MODE_PRIVATE)
        return sp.getBoolean("isAuth", false)
    }

    private fun getApiKey(): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("api_key", null)
    }

    private fun getRefreshToken(): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("refresh_token", null)
    }
}
