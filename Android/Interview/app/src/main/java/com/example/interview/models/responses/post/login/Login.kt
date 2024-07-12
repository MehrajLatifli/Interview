package com.example.interview.models.responses.post.login


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("Password")
    val password: String,
    @SerializedName("Username")
    val username: String
)