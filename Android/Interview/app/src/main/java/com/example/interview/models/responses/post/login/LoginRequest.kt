package com.example.interview.models.responses.post.login


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("Password")
    val password: String,
    @SerializedName("Username")
    val username: String
)