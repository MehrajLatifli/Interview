package com.example.interview.source.api

import android.content.Context
import android.os.HandlerThread
import android.util.Log
import com.example.interview.models.responses.post.token.RefreshTokenRequest
import com.example.interview.source.api.IApiManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RefreshTokenDetector @Inject constructor(private val apiManager: IApiManager, private val context: Context) {

    private var job: Job? = null
    private var handlerThread: HandlerThread? = null

    init {
        handlerThread = HandlerThread("RefreshTokenHandlerThread").apply { start() }
        startTokenRefreshing()
    }

    fun startTokenRefreshing() {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(10 * 1000)

            while (isActive) {

                if(getUserAuth()==true) {
                delay(10 * 1000) // Every 10 seconds
                refreshToken()
                }
                else
                {

                }
            }

        }
    }

    private suspend fun refreshToken() {
        try {
            // Retrieve current tokens from SharedPreferences
            val accessToken = getApiKey()
            val refreshToken = getRefreshToken()

            if(getUserAuth()==true) {
                if (accessToken != null && refreshToken != null) {

                    // Perform refresh token request
                    val response =
                        apiManager.refreshToken(RefreshTokenRequest(accessToken, refreshToken))
                    if (response.isSuccessful) {
                        val tokenResponse = response.body()
                        if (tokenResponse != null) {
                            // Update tokens in SharedPreferences or handle as needed
                            saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                            Log.e("RefreshTokenDetector", "Tokens refreshed successfully")
                        } else {

                            Log.e("RefreshTokenDetector", "Token response body is null")
                        }
                    } else {

                        Log.e(
                            "RefreshTokenDetector",
                            "Failed to refresh tokens ${response.code()} - ${response.message()}"
                        )
                    }
                } else {
                    println("Access token or refresh token is null")

                    Log.e("RefreshTokenDetector", "Access token or refresh token is null")
                }
            }
        } catch (e: Exception) {
            println("Exception refreshing tokens: ${e.message}")
        }
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        // Save tokens to SharedPreferences or secure storage as needed
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("api_key", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    fun stop() {
        job?.cancel() // Cancels the coroutine job
        handlerThread?.quitSafely() // Quits the handler thread
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
