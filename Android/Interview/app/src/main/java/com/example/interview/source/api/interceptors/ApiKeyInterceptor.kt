package com.example.interview.source.api.interceptors

import com.example.interview.source.api.ApiKeyProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(private val apiKeyProvider: ApiKeyProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val apiKey = apiKeyProvider.getApiKey() ?: ""
        val authorizedRequest = request.newBuilder()
            .header("Authorization", "Bearer $apiKey")
            .build()
        return chain.proceed(authorizedRequest)
    }
}



