package com.example.interview.source.api

import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.models.responses.post.login.Login
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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


    @GET("Auth/profile")
    suspend fun getprofile(
    ):Response<ProfileResponse>


}