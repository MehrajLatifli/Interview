package com.example.interview.source.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IApiManager {

    @Multipart
    @POST("Auth/registerAdmin")
    suspend fun createAccount(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part imagePath: MultipartBody.Part? // Use MultipartBody.Part for the file
    ): Response<Unit>


}