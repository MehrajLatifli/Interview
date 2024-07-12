package com.example.interview.source.api

import com.example.interview.utilities.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept (chain: Interceptor. Chain): Response {

        val request = chain.request()

        val authorizedRequest = request.newBuilder().header( "Authorization", "Bearer ${API_KEY}").build()
        return chain. proceed (authorizedRequest)


        return chain.proceed(request)
    }
}