package com.example.interview.models.responses.get.profile


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("imagePath")
    val imagePath: String,
    @SerializedName("permitions")
    val permitions: List<Permition>,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("username")
    val username: String
)