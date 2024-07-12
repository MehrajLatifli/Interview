package com.example.interview.source.api

import com.example.interview.models.responses.get.LoginResponse
import com.example.interview.models.responses.post.login.Login
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IApiManager {

    @Multipart
    @POST("Auth/registerAdmin")
    suspend fun registerAdmin(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part imagePath: MultipartBody.Part?
    ): Response<Unit>

    @Multipart
    @POST("Auth/registerHR")
    suspend fun registerHR(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part imagePath: MultipartBody.Part?
    ): Response<Unit>


    @POST("Auth/login")
    suspend fun login(@Body login: Login): Response<LoginResponse>


}