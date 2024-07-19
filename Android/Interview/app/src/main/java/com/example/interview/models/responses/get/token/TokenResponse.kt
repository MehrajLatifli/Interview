package com.example.interview.models.responses.get.token

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
