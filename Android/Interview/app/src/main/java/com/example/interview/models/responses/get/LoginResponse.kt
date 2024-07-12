package com.example.interview.models.responses.get

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("expiration") val expiration: String
)