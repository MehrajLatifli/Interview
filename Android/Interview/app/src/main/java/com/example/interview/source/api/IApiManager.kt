package com.example.interview.source.api

import com.example.interview.models.responses.get.candidate.CandidateDocumentResponse
import com.example.interview.models.responses.get.candidate.CandidateResponse
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.models.responses.get.token.TokenResponse
import com.example.interview.models.responses.post.candidate.Candidate
import com.example.interview.models.responses.post.login.Login
import com.example.interview.models.responses.post.token.RefreshTokenRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiManager {

    @Headers("Content-Type: application/json; charset=UTF-8")


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


    @POST("Auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("Auth/refreshToken")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<TokenResponse>


    @Multipart
    @POST("CandidateDocument/candidatedocument")
    suspend fun registerCandidatedocument(
        @Part("surname") surname: RequestBody,
        @Part("name") name: RequestBody,
        @Part("patronymic") patronymic: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("email") email: RequestBody,
        @Part cv: MultipartBody.Part?,
        @Part("address") address: RequestBody
    ): Response<Unit>

    @POST("Candidate/candidate")
    suspend fun registerCandidate(@Body candidate: Candidate): Response<CandidateResponse>

    @GET("CandidateDocument/candidatedocument")
    suspend fun getAllCandidateDocuments(): Response<List<CandidateDocumentResponse>>

    @GET("CandidateDocument/candidatedocument/{id}")
    suspend fun getCandidateDocumentByID(
        @Path("id") id: Int
    ): Response<CandidateDocumentResponse>


    @GET("Candidate/candidate")
    suspend fun getAllCandidates(): Response<List<CandidateResponse>>

    @DELETE("CandidateDocument/candidatedocument/{id}")
    suspend fun deleteCandidateDocumentById(
        @Path("id") id: Int
    ): Response<Unit>

    @Multipart
    @PUT("CandidateDocument/candidatedocument")
    suspend fun updateCandidateDocument(
        @Part("id") id: RequestBody?,
        @Part("surname") surname: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("patronymic") patronymic: RequestBody?,
        @Part("phoneNumber") phoneNumber: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part cv: MultipartBody.Part?,
        @Part("address") address: RequestBody?
    ): Response<Unit>


}