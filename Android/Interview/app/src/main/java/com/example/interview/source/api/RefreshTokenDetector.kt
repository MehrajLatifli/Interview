package com.example.interview.source.api

import android.content.Context
import android.content.Intent
import android.os.HandlerThread
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.post.token.RefreshTokenRequest
import com.example.interview.source.api.IApiManager
import com.example.interview.views.fragments.auth.login.LogInFragment
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

            refreshToken()


            delay(1 * 1000)


                while (isActive) {
                    delay(30 * 60 * 1000)
                    try {
                        semaphore.acquire()
                        refreshToken()
                    } finally {
                        semaphore.release()
                    }
                }
        }
    }

    private suspend fun refreshToken() {
        try {
            val accessToken = getApiKey()
            val refreshToken = getRefreshToken()

            if(apiManager.getprofile().body()==null){

                clearTokens()
            }


            if (accessToken != null && refreshToken != null) {
                val response = apiManager.refreshToken(RefreshTokenRequest(accessToken, refreshToken))
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null) {
                        saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                        Log.d("RefreshTokenDetector", "Tokens refreshed successfully")
                    } else {
                        Log.e("RefreshTokenDetector", "Token response body is null")
                    }
                } else {
                    handleTokenRefreshFailure(response.code())
                }
            } else {
                Log.e("RefreshTokenDetector", "Access token or refresh token is null")
                handleTokenRefreshFailure(401)
            }
        } catch (e: Exception) {
            Log.e("RefreshTokenDetector", "Exception refreshing tokens: ${e.message}")
            handleTokenRefreshFailure(500)
        }
    }

    private fun handleTokenRefreshFailure(statusCode: Int) {
        when (statusCode) {
            401 -> {
                Log.e("RefreshTokenDetector", "Unauthorized access - Tokens might be expired")
                clearTokens()
                navigateToLogin()
            }
            else -> {
                Log.e("RefreshTokenDetector", "Failed to refresh tokens: HTTP $statusCode")
            }
        }
    }

    private fun clearTokens() {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("api_key")
            remove("refresh_token")
            apply()
        }

        val sharedPreferences2 = context.getSharedPreferences("authresult_local", Context.MODE_PRIVATE)
        with(sharedPreferences2.edit()) {
            remove("isAuth")
            apply()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(context, LogInFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(context, intent, null)
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
