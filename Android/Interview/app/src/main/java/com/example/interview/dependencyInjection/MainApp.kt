package com.example.interview.dependencyInjection

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.interview.workers.TokenRefreshWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // WorkManager üçün iş tələbi yaradın
        val workRequest = PeriodicWorkRequestBuilder<TokenRefreshWorker>(30, TimeUnit.MINUTES)
            .build()

        // WorkManager-ə iş əlavə edin
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TokenRefreshWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

}