package com.example.interview.models.responses.post.token

data class RefreshTokenRequest(
    val accessToken: String,
    val refreshToken: String
)
