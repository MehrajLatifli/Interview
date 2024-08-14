package com.example.interview.models.responses.get.profile


import com.google.gson.annotations.SerializedName

data class UserClaim(
    @SerializedName("claimType")
    val claimType: String,
    @SerializedName("claimValue")
    val claimValue: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int
)