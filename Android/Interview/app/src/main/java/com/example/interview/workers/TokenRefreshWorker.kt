package com.example.interview.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenRefreshWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val apiManager: IApiManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            val sharedPreferences = applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val currentToken = sharedPreferences.getString("api_key", null)

            if (currentToken != null) {
                val response = apiManager.refreshToken(currentToken)
                if (response is Resource.Success) {
                    val newToken = response.data?.token
                    if (newToken != null) {
                        sharedPreferences.edit().putString("api_key", newToken).apply()
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
